(ns ambassador.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [ambassador.db :as db]
            [clojure.data.json :as json]
            [postal.core :as postal]
            [ambassador.properties :as props]))

(defn give
  [_]
  {:status 501
   :body   "Not yet implemented"})

(defn bible
  [_]
  {:status 501
   :body   "Bible verses are not available here yet"})

(defn contact
  [_]
  (postal/send-message {:host (:host props/email)
                        :user (:user props/email)
                        :pass (:pass props/email)
                        :port (:port props/email)
                        :tls  true}
                       {:from    "daniel.r.heiniger@gmail.com"
                        :to      "daniel.r.heiniger@gmail.com"
                        :subject "Testing from Clojure app"
                        :body    "Test."})
  {:status 202
   :body   "Confirmed"})

(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/give" :get (conj common-interceptors `give)]
              ["/bible" :get (conj common-interceptors `bible)]
              ["/contact" :post (conj common-interceptors `contact)]})


;; Consumed by ambassador.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env                     :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes            routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path     "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type              :jetty
              ::http/host              "0.0.0.0"            ;;required for Docker
              ::http/port              8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false
                                        ;; Alternatively, You can specify you're own Jetty HTTPConfiguration
                                        ;; via the `:io.pedestal.http.jetty/http-configuration` container option.
                                        ;:io.pedestal.http.jetty/http-configuration (org.eclipse.jetty.server.HttpConfiguration.)
                                        }})
