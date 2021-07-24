(ns ambassador.db
  (:require [next.jdbc :as jdbc]
            [ambassador.properties :as p]
            [clojure.string :as str]
            [clojure.spec.alpha :as spec]
            [net.cgrand.enlive-html :as html]))

(def db-props p/db)

(def db {:dbtype               (:dbtype db-props)
         :host                 (:host db-props)
         :dbname               (:dbname db-props)
         :user                 (:user db-props)
         :password             (:password db-props)
         :zeroDateTimeBehavior "convertToNull"})

(def ds (jdbc/get-datasource db))
(defn get-tables []
  (jdbc/execute! ds ["SHOW TABLES;"]))