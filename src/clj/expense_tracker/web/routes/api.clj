(ns expense-tracker.web.routes.api
  (:require
   [integrant.core :as ig]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [expense-tracker.recipe-scraping.enlive :as enlive]))

(defmethod ig/init-key :router/routes
  [_ value]
  (println "Initializing routes")
  [["/api" (fn [_] {:status 200 :body "Hello"})]
   ["" {:no-doc true}
    ["/swagger.json" {:get (swagger/create-swagger-handler)}]
    ["/api-docs/*" {:get (swagger-ui/create-swagger-ui-handler)}]
    ["/get-recipe" {:get {:parameters
                          {:query [:map [:recipeURL string?]]}
                          :handler enlive/parse-recipe-controller}}]]])
