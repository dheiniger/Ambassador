(ns ambassador.properties
  (:require [ambassador.vault :as vault]))

(def secrets (vault/read-secrets))

(def db
  {:dbtype (:db.dbtype secrets)
   :host (:db.host secrets)
   :dbname (:db.dbname secrets)
   :user (:db.user secrets)
   :password (:db.password secrets)})

(def email
  {:host (:message.email.host secrets)
   :user (:message.email.user secrets)
   :pass (:message.email.pass secrets)
   :port 587
   :tls  true})

(def spotify
  {:api-url "https://api.spotify.com"
   :authorization-url "https://accounts.spotify.com/api/token"
   :client-id (:spotify.client_id secrets)
   :client-secret (:spotify.client_secret secrets)})