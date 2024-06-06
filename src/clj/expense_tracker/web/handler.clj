(ns expense-tracker.web.handler
  (:require
   [integrant.core :as ig]
   [muuntaja.core :as m]
   [reitit.coercion.malli :as malli]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

(defmethod ig/init-key :handler/ring
  [_ {:keys [router]}]
   (ring/ring-handler router (ring/create-default-handler))
  )

(defmethod ig/init-key :router/core
  [_ {:keys [routes]}]
  (ring/router
   routes
   {:data {:coercion malli/coercion
           :muuntaja m/instance
           :middleware [
                        ;; query-params & form-params
                        parameters/parameters-middleware
                        ;; request/response middleware
                        muuntaja/format-middleware
                        ;; coercion
                        coercion/coerce-exceptions-middleware
                        coercion/coerce-request-middleware
                        coercion/coerce-response-middleware
                        ]}}))
