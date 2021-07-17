(ns ambassador.vault
  (:require [vault.core :as vault]
            [vault.client.http]
            [vault.secrets.kvv2 :as vault-kvv2]))

(def client (vault/new-client "http://vault.wikstro.com:8200"))

(def props-file
  (try
    (load-file "vault.edn")
    (catch Exception e (prn (str "Could not find file: " (.getMessage e)))
                       (load-file "src/ambassador/vault.edn"))))

(defn authenticate []
  (let [user (:username props-file)
        password (:password props-file)]
    (vault/authenticate! client :userpass {:username user, :password password})))

(defn read-secrets []
  (authenticate)
  (vault-kvv2/read-secret client "kv" "Ambassador"))

