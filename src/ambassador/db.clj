(ns ambassador.db
  (:require [next.jdbc :as jdbc]
            [ambassador.properties :as props]
            [clojure.string :as str]
            [clojure.spec.alpha :as spec]
            [net.cgrand.enlive-html :as html])
  (:import (java.text SimpleDateFormat)))

(def db-props props/db)
(def db {:dbtype               (:dbtype db-props)
         :host                 (:host db-props)
         :dbname               (:dbname db-props)
         :user                 (:user db-props)
         :password             (:password db-props)
         :zeroDateTimeBehavior "convertToNull"})

(def ds (jdbc/get-datasource db))

(defn make-post-key-prefix [siteid table]
  (str "wp_" siteid "_" table))

(defn make-attribute-keyword [siteid table attribute]
  (keyword (make-post-key-prefix siteid table) (str/replace attribute "-" "_")))

(defn clean-post-results [site-id results]
  (let [table "posts"
        post-id-key (make-attribute-keyword site-id table "ID")
        post-title-key (make-attribute-keyword site-id table "post_title")
        post-date-key (make-attribute-keyword site-id table "post_date_gmt")
        post-content-key (make-attribute-keyword site-id table "post_content")
        simplified-posts (map #(assoc {} :post-id (post-id-key %)
                                         :post-title (post-title-key %)
                                         :post-date (.format (SimpleDateFormat. "MM/dd/yyyy hh:mmaa") (post-date-key %))
                                         :post-content (post-content-key %)) results)]
    simplified-posts))

(defn retrieve-pages [site-id]
  (if-not (spec/valid? integer? site-id)
    (throw (Exception. "Site ID must be an integer"))
    (let [results (jdbc/execute! ds [(str "select * from wp_" site-id "_posts
                          where post_type='page'
                          and post_status ='publish'")])]
      (clean-post-results site-id results))))

(defn retrieve-posts [site-id page-size page-number]
  (if-not (spec/valid? integer? site-id)
    (throw (Exception. "Site ID must be an integer")))
  (time (let [safe-page-size (if (string? page-size) (Integer/parseInt page-size)
                                                     (or page-size 10))
              safe-page-number (if (string? page-number) (Integer/parseInt page-number)
                                                         (or page-number 1))
              row-start (* (- (or safe-page-number 1) 1) safe-page-size)
              posts (jdbc/execute! ds [(str "select * from wp_" site-id "_posts
                          where post_type='post'
                          and post_status ='publish'
                          order by post_date desc
                          limit " row-start ", " safe-page-size)])]
          (clean-post-results site-id posts))))


(defn retrieve-post [site-id post-id]
  (if-not (spec/valid? integer? site-id)
    (throw (Exception. "Site ID must be an integer")))
  (if-not (spec/valid? integer? post-id)
    (throw (Exception. "Post ID must be an integer"))
    (let [posts (jdbc/execute! ds [(str "select * from wp_" site-id "_posts
                          where post_type='post'
                          and post_status ='publish'
                          and ID =" post-id)])]
      (clean-post-results site-id posts))))

(defn retrieve-page [siteid page-name]
  (let [pages (retrieve-pages siteid)
        post-title-key (make-attribute-keyword siteid "posts" "post_title")
        page-name-filter #(= page-name (post-title-key %))]
    (first (filter page-name-filter pages))))

(defn retrieve-sermons [siteid]
  (retrieve-page siteid "Sermons"))

(defn test-queries [size page-num]
  (time (let [row-start (* (- page-num 1) size)]

          (jdbc/execute! ds [(str "SELECT * FROM WP_" 12 "_POSTS
                          WHERE  POST_STATUS ='publish'
                          AND POST_TYPE = 'post'
                          order by post_date desc
                          limit " row-start ", " size)]))))
