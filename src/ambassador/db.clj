(ns ambassador.db
  (:require [next.jdbc :as jdbc]
            [ambassador.properties :as props]
            [clojure.string :as str]
            [clojure.spec.alpha :as spec]))

(def db-props props/db)
(def db {:dbtype               (:dbtype db-props)
         :host                 (:host db-props)
         :dbname               (:dbname db-props)
         :user                 (:user db-props)
         :password             (:password db-props)
         :zeroDateTimeBehavior "convertToNull"})

(def ds (jdbc/get-datasource db))

(defn make-post-key-prefix [siteid]
  (str "wp_" siteid "_posts"))

(defn make-attribute-keyword [siteid attribute]
  (keyword (make-post-key-prefix siteid) (str/replace attribute "-" "_")))

(defn retrieve-pages [siteid]
  (if-not (spec/valid? integer? siteid)
    (throw (Exception. "Site ID must be an integer"))
    (jdbc/execute! ds [(str "select * from wp_" siteid "_posts
                          where post_type='page'
                          and post_status ='publish'")])))

(defn retrieve-page [siteid page-name]
  (let [pages (retrieve-pages siteid)
        post-title-key (make-attribute-keyword siteid "post_title")
        page-name-filter #(= page-name (post-title-key %))]
    (first (filter page-name-filter pages))))

(defn retrieve-page-content [siteid page-name]
  ((make-attribute-keyword siteid "post-content") (retrieve-page siteid page-name)))

(defn retrieve-blog [siteid]
  (retrieve-page siteid "Blog"))

(defn retrieve-sermons [siteid]
  (retrieve-page siteid "Sermons"))



