(ns ambassador.spotify
  (:require [clj-http.client :as client]
            [ambassador.properties :as p]
            [ambassador.util :as util]
            [clojure.data.json :as json]
            [io.pedestal.log :as log]))

(defn create-auth-token []
  (let [spotify-client-id (:client-id p/spotify)
        spotify-client-secret (:client-secret p/spotify)]
    (log/info :msg "Building Spotify Authorization token from client-id and client-secret...")
    "Authorization" (str "Basic " (util/base64-encode (str spotify-client-id ":" spotify-client-secret)))))

(defn create-request-map []
  {:form-params {:grant_type "client_credentials"}
   :headers     {:content-type   "json"
                 "Authorization" (create-auth-token)}})

(defn retrieve-spotify-access-token []
  (log/info :msg "Requesting Spotify access token...")
  (let [spotify-authorization-url (:authorization-url p/spotify)
        response-body (json/read-str (:body (client/post spotify-authorization-url (create-request-map)))
                                     :key-fn keyword)]
    (:access_token response-body)))

