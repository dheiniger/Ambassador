(ns ambassador.properties)

(def props-file
  (try
    (load-file "info.txt")
    (catch Exception e (prn (str "Could not find file: " (.getMessage e)))
                       (load-file "src/ambassador/info.txt"))))

(def db-props (:db props-file))
(def email-props (:email props-file))

(def db
  {:dbtype   (:dbtype db-props)
   :host     (:host db-props)
   :dbname   (:dbname db-props)
   :user     (:user db-props)
   :password (:password db-props)})

(def email
  {:host (:host email-props)
   :user (:user email-props)
   :pass (:pass email-props)
   :port (:port email-props)
   :tls  true})