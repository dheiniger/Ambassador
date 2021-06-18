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


;;GIVING
;;SERMONS
;;BLOG
;;EVENTS
;;DAILY SCRIPTURE
;;HOME GROUPS/BIBLE STUDY INFORMATION (SIGNON??)
;;BIBLE
;;FIND US/CONTACT US
;;GROUP CHAT
;;GOSPEL MESSAGE
;;SHARING APP
;;SCHEDULE APPOINTMENT
;;NOMINATIONS/BALLOTS
;;NEWS/BULLETIN
;;POLLS/SURVEYS
;;REFNET LINK?


(defn get-tables []
  (jdbc/execute! ds ["SHOW TABLES;"]))