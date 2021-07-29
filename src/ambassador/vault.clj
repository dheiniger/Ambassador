(ns ambassador.vault
  (:require [vault.core :as vault]
            [vault.client.http]
            [vault.secrets.kvv2 :as vault-kvv2]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(def client (vault/new-client "http://vault.wikstro.com:8200"))

(defn authenticate []
  (let [user (env :vusername)
        password (env :vpassword)]
    (log/info "Authenticating Vault")
    (vault/authenticate! client :userpass {:username user, :password password})))

(defn read-secrets []
  (authenticate)
  (log/info "Reading secrets from Vault")
  (vault-kvv2/read-secret client "kv" "Ambassador"))

