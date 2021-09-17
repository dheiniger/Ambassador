(defproject ambassador "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.9"]
                 [cheshire "5.10.0"]                        ;;Force newer version for vault-clj
                 [io.pedestal/pedestal.jetty "0.5.8"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]
                 [mysql/mysql-connector-java "8.0.24"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [org.clojure/data.json "1.0.0"]
                 [clj-http "3.12.3"]
                 [enlive "1.1.6"]
                 [hickory "0.7.1"]
                 [amperity/vault-clj "1.0.3"]
                 [com.draines/postal "2.0.4"]
                 [environ "1.2.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :profiles {:dev     {:aliases      {"run-dev" ["trampoline" "run" "-m" "ambassador.server/run-dev"]}
                       :dependencies [[io.pedestal/pedestal.service-tools "0.5.8"]]}
             :uberjar {:aot [ambassador.server]}}
  :main ^{:skip-aot true} ambassador.server)
