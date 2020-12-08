(ns aoc-2020.day-seven
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.edn :as edn]))

(def colored-bag-rules-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/colored_bag_rules.txt")

;; parse the input file into a collection of maps
;; {:main-bag-1 (:sub-bag-1 :sub-bag-2) :main-bag-2 (:sub-bag-1 :sub-bag-2) ...}
(defn get-map-entry-no-values [raw-bags-string]
  (let [raw-bag-string (str/replace raw-bags-string #"bags" "bag")
        parsed-raw-bags-string (str/replace raw-bag-string #"^(.* bag)\scontain\s(.*)(.)" "$1#$2, ")
        main-bag (str/replace (str/replace parsed-raw-bags-string #"(.*)#.*" "$1") #" " "-")
        sub-bags-with-values (map #(str/replace % #"(\w) " "$1-") (re-seq #"\d[\w|\s]+" parsed-raw-bags-string))
        sub-bags (map #(str/replace % #"\d-([\w|-]+)" "$1") sub-bags-with-values)]
    (clojure.edn/read-string (str "{:" main-bag " (:" (str/join " :" sub-bags) ")}"))
    ))

;; for a given desired bag type (e.g. "pale-bronze-bag")
;; determine if the given bag-map can contain it
(defn does-contain-bag [bag-map desired-bag]
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
                     (does-contain-bag bag-map desired-bag)))))))

;; for a given starting list of bag types, find every bag that contains those
;; bag types, and every bag that contains those, etc...
(defn find-all-containing-bags
  ([full-bag-list search-for-containers] (find-all-containing-bags full-bag-list search-for-containers ()))
  ([full-bag-list search-for-containers final-list]
   (let [bags-contained-in (remove nil? (map #(get-all-possible-containers % search-for-containers) full-bag-list))]
     (if (empty? bags-contained-in)
       final-list
       (find-all-containing-bags full-bag-list (map #(str (first %)) bags-contained-in) (conj final-list (map #(str (first %)) bags-contained-in)))))))

;; this is slow and inefficient, but it works
(defn aoc-day7-p1 []
  (let [colored-bag-rules    (slurp colored-bag-rules-file)
        modified-bc (map #(str/replace % #"\sno\s" " 0 ") (str/split-lines colored-bag-rules))
        full-bag-list (map get-map-entry-no-values modified-bc)
        all-shiny-bag-containers (find-all-containing-bags full-bag-list (list ":shiny-gold-bag"))]
    (count (set (flatten all-shiny-bag-containers)))))

;; parse the input file into a collection of maps
;; {:main-bag-1 (:sub-bag-1 :sub-bag-2) :main-bag-2 (:sub-bag-1 :sub-bag-2) ...}
(defn get-map-entry-with-values [raw-bags-string]
  (let [raw-bag-string (str/replace raw-bags-string #"bags" "bag")
        parsed-raw-bags-string (str/replace raw-bag-string #"^(.* bag)\scontain\s(.*)(.)" "$1#$2, ")
        main-bag (str/replace (str/replace parsed-raw-bags-string #"(.*)#.*" "$1") #" " "-")
        sub-bags-with-values (map #(str/replace % #"(\w) " "$1-") (re-seq #"\d[\w|\s]+" parsed-raw-bags-string))
        sub-bags (map #(str/replace % #"(\d)-(.*)" ":$2 $1") sub-bags-with-values)]
    (clojure.edn/read-string (str "{:" main-bag " {" (str/join " " sub-bags) "}}"))))

;; you can't use recursion with do-seq because it returns nil
;; (defn find-total-bags-within-do-seq
;;   ([full-bag-list main-bag] (find-total-bags-within-do-seq full-bag-list main-bag ()))
;;   ([full-bag-list main-bag accumulated-values]
;;    (let [sub-bag-list (into {} (get full-bag-list main-bag))]
;;     ;;  (println "sub-bag-list: " sub-bag-list)
;;        (doseq [bag sub-bag-list]
;;         ;;  (println "bag: " bag " sub-bag-list: " sub-bag-list)
;;          (let [num-bags (get-in full-bag-list [main-bag (key bag)])]
;;           ;;  (println main-bag " " bag " " num-bags)
;;            (if (= num-bags 0)
;;              (do
;;                (println "finished searching: " accumulated-values)
;;                num-bags)
;;              (find-total-bags-within full-bag-list (key bag) (conj (list num-bags) accumulated-values))
;;             ;;  (println "output of func: " (find-total-bags-within full-bag-list (key bag)))
;;             ;; (+ num-bags (* num-bags (find-total-bags-within full-bag-list (key bag) (conj (list num-bags) accumulated-values))))
;;              ))))))

;; (defn find-total-bags-within-loop
;;   [full-bag-list main-bag]
;;      (loop [sub-bag-list (into {} (get full-bag-list main-bag))
;;             final-counts ()]
;;        (if (empty? sub-bag-list)
;;          final-counts
;;          (let [[bag & remaining] sub-bag-list]
;;            (recur (into {} remaining)
;;                   (into final-counts 
;;                         (list (get-in full-bag-list [main-bag (key bag)])))))
;;        ) ;; if
;;        )) ;; loop

;; each element: (:[\w|-]+\s\d)
(defn compute-total [bag-list]
    (let [final-bag-map (into {} (clojure.edn/read-string (str/replace bag-list #"(:[\w|-]+\s\d)" "{$1}")))
          final-values ()]
      (println final-bag-map)
      ;; (for [bag final-bag-map]
      ;;   (println "bag: " bag " first: " (first bag) " second: " (second bag))
      ;;   )
      )
  )

(defn find-total-bags-within 
  ([full-bag-list main-bag] (find-total-bags-within full-bag-list main-bag {}))
  ([full-bag-list main-bag final-bag-list]
  (let [sub-bag-list (into {} (get full-bag-list main-bag))]
    (if (empty? sub-bag-list)
      (do
        (println "===========")
        (compute-total final-bag-list))
      (for [bag sub-bag-list]
        (let [num-bags (get-in full-bag-list [main-bag (key bag)])]
          ;;(if (= num-bags 0)
            ;;(find-total-bags-within full-bag-list (key bag))
          (find-total-bags-within full-bag-list (key bag) (into bag final-bag-list))
            ;; (+ num-bags (* num-bags (find-total-bags-within full-bag-list (key bag))))
            ;;)
          ))))))

;; doesn't work
;; 45157
(defn aoc-day7-p2 []
    (let [colored-bag-rules    (slurp colored-bag-rules-file)
          modified-bc (map #(str/replace % #"\sno\s" " 0 ") (str/split-lines colored-bag-rules))
          full-bag-list (into {} (map get-map-entry-with-values modified-bc))
          shiny-gold-bag-contents (get full-bag-list :shiny-gold-bag)]
      (find-total-bags-within full-bag-list :shiny-gold-bag);; shiny-gold-bag-contents)
      ;; (println full-bag-list)
      ;; (println shiny-gold-bag-contents)
      ;; (println (get full-bag-list :posh-salmon-bag))
      ;; (println (get full-bag-list :shiny-brown-bag))
      ;; (println (get-in full-bag-list [:posh-salmon-bag :shiny-brown-bag])) ;; returns value for :shiny-.. in :posh-..
      ;; (println (select-keys full-bag-list [:posh-salmon-bag])) ;; returns {key map}
      )
)