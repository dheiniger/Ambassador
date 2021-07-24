(ns ambassador.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [ambassador.service :as service]
            [environ.core :refer [env]]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest home-page-test
  (prn "Testing now...")
 true)

(deftest about-page-test
  true)
