(ns ambassador.db
  (:require [next.jdbc :as jdbc]
            [ambassador.properties :as props]))


(def db-props props/db)
(def db {:dbtype (:dbtype db-props)
         :host (:host db-props)
         :dbname (:dbname db-props)
         :user (:user db-props)
         :password (:password db-props)})
(def ds (jdbc/get-datasource db))


(defn test-connection []
  (jdbc/execute! ds ["select * from wp_users"]))
