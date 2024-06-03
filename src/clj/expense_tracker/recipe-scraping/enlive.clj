(ns expense-tracker.recipe-scraping.enlive
  (:require [net.cgrand.enlive-html :as html]))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn parse-ingredient [ingredient])

(comment
    (def page-content
      (fetch-url "https://streetkitchen.hu/fantasztikus-desszertek/cukormentes-epres-muffin/"))
    (def ingredient
      (first          (html/select page-content
                 #{
                   [:dd]})))
  )
