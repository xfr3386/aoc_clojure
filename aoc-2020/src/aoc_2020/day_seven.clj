(ns aoc-2020.day-seven
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.edn :as edn]))

(def colored-bag-rules-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/colored_bag_rules.txt")

(defn get-map-entry-no-values [raw-bags-string]
  (let [raw-bag-string (str/replace raw-bags-string #"bags" "bag")
        parsed-raw-bags-string (str/replace raw-bag-string #"^(.* bag)\scontain\s(.*)(.)" "$1#$2, ")
        main-bag (str/replace (str/replace parsed-raw-bags-string #"(.*)#.*" "$1") #" " "-")
        sub-bags-with-values (map #(str/replace % #"(\w) " "$1-") (re-seq #"\d[\w|\s]+" parsed-raw-bags-string))
        sub-bags (map #(str/replace % #"\d-([\w|-]+)" "$1") sub-bags-with-values)]
    (clojure.edn/read-string (str "{:" main-bag " (:" (str/join " :" sub-bags) ")}"))
    ))

;; for a given desired-bag bag type (e.g. "pale-bronze-bag")
;; return all bags that can contain that bag
(defn get-containing-bags [bag-map desired-bag]
  (if (re-find (re-pattern desired-bag) (apply str (vals bag-map)))
    (keys bag-map)
    nil))

;; For a collection of bags types, return all bags that
;; contain those bags
(defn get-all-possible-containers [bag-map desired-bags]
  (loop [remaining-bags desired-bags
         final-bag-list []]
    (if (empty? remaining-bags)
      (if (empty? final-bag-list)
        nil
        final-bag-list)
      (let [[desired-bag & remaining] remaining-bags]
        (recur remaining
               (into final-bag-list
                     (get-containing-bags bag-map desired-bag)))))))

;; for a given starting list of bag types, find every bag that contains those
;; bag types, and every bag that contains those, etc...
(defn find-all-containing-bags
  ([full-bag-list search-for-containers] (find-all-containing-bags full-bag-list search-for-containers ()))
  ([full-bag-list search-for-containers final-list]
   (let [bags-contained-in (remove nil? (map #(get-all-possible-containers % search-for-containers) full-bag-list))]
     (if (empty? bags-contained-in)
       final-list
       (find-all-containing-bags full-bag-list (map #(str (first %)) bags-contained-in) (conj final-list (map #(str (first %)) bags-contained-in)))))))

(defn aoc-day7-p1 []
  (let [colored-bag-rules    (slurp colored-bag-rules-file)
        modified-bc (map #(str/replace % #"\sno\s" " 0 ") (str/split-lines colored-bag-rules))
        full-bag-list (map get-map-entry-no-values modified-bc)
        all-shiny-bag-containers (find-all-containing-bags full-bag-list (list ":shiny-gold-bag"))]
    (count (set (flatten all-shiny-bag-containers)))))
