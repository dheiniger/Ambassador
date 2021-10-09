(ns ambassador.util
  (:import (java.util Base64)))

(defn base64-encode [to-encode]
  (String. (.encode (Base64/getEncoder)
                    (.getBytes to-encode))
                    "UTF-8"))
