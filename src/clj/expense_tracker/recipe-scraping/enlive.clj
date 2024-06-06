#_{:clj-kondo/ignore [:namespace-name-mismatch]}
(ns expense-tracker.recipe-scraping.enlive
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [ring.util.http-response :as response]
   ))

(defn- fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn- split-on-space [word]
  (string/split word #"\s+"))

(defn- squish [line]
  (string/triml (string/join " "
     (split-on-space (string/replace line #"\n" " ")))))

(defn- sanitize-ingredient-vector [ingredient-vector]
  (filter (fn [ingredient-vector]
            (and (not (= ingredient-vector "\n- "))
                 (not (= ingredient-vector "\n"))
                 (= (-> ingredient-vector
                        :attrs
                        :data-idqdefault2) nil)))
          ingredient-vector))

(defn- extract-ingredient-quantity [ingredient-span-tag]
  (parse-double (-> ingredient-span-tag
                  :attrs
                  :data-idqdefault)))

(defn- extract-ingredient-unit-and-name [ingredient-content-string ingredient-has-quantity?]
  (let [ingredient-content-seq (-> ingredient-content-string
                                   squish
                                   split-on-space)]
    (if ingredient-has-quantity?
      {:unit (first ingredient-content-seq)
       :ingredient-name (string/join " " (rest ingredient-content-seq))}
      {:unit nil
       :ingredient-name (string/join " " ingredient-content-seq)})))


(defn parse-ingredient [ingredient]
  (let [ingredient-vector (sanitize-ingredient-vector (:content ingredient))
        default-quantity (extract-ingredient-quantity (first ingredient-vector))
        ingredient-unit-and-name (extract-ingredient-unit-and-name (second ingredient-vector) (not (nil? default-quantity)))]
    (conj {:default-quantity default-quantity} ingredient-unit-and-name)))

(defn parse-ingredients [page-content]
  (as-> page-content content
    (html/select content #{[:dd]})
    (map parse-ingredient content)))

(defn parse-title [page-content]
  (-> page-content
      (html/select #{[:h1.entry-title]})
      first
      html/text
      squish))

(defn parse-steps [page-content]
  (as-> page-content content
    (html/select content #{[:div.the-content-div :p]})
    (map html/text content)
    (take-while #(not (string/starts-with? % "Ha tetszett")) content)))

(defn parse-recipe [page-url]
  (let [page-content (fetch-url page-url)]
    {:ingredients (parse-ingredients page-content)
     :title (parse-title page-content)
     :steps (parse-steps page-content)}))

(defn parse-recipe-controller [req]
  (let [stuff (get-in req [:parameters :query :recipeURL])]
    (response/ok
     (parse-recipe stuff))))


(comment
  (def page-content
    (fetch-url "https://streetkitchen.hu/hust-hussal/mustaros-tokany/"))

  (squish (html/text (first (html/select page-content #{[:div.entry-lead]}))))

  (def page-content (fetch-url "https://streetkitchen.hu/instant/egyszeru-levesek/avokadokremleves/"))

  (map #(-> % html/text) (html/select page-content #{[:div.the-content-div :p]}))

  (parse-recipe "https://streetkitchen.hu/instant/egyszeru-levesek/avokadokremleves/")

  (def ingredients
    (html/select page-content
                 #{[:dd]}))

  (count (filter   ingredient))

  (def ingredient (:content (nth ingredients 8)))

  (and (not (= (nth ingredient 0) "\n- "))
       (= (-> (nth ingredient 0)
              :attrs
              :data-idqdefault2) nil))

  (count ingredient)

  (-> (nth (:content ingredient) 1)
      :attrs
      :data-idqdefault)
  (string/split (nth (:content ingredient) 2) #"\n")
  (parse-ingredient  (first ingredients))

  (map parse-ingredient ingredients))
