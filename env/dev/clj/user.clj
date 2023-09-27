(ns user
  (:require
   [clojure.pprint]
   [integrant.repl :refer [clear go halt prep init reset reset-all]]
   [integrant.core :as ig]
   [clojure.tools.namespace.repl :as repl]
   [lambdaisland.classpath.watch-deps :as watch-deps]
   [expense-tracker.core :refer [start-app]]
   ))

(watch-deps/start! {:aliases [:dev :test]})

(add-tap (bound-fn* clojure.pprint/pprint))

(integrant.repl/set-prep! #(-> (ig/read-string (slurp "resources/system.edn"))
                               (ig/prep)))


(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)
