(ns day-seven
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

(defn get-containing-bags [bag-map desired-bag]
  ;; for a given desired-bag bag type (e.g. "pale-bronze-bag")
  ;; return all bags that can contain that bag
  ;; (let [set-of-values (set (vals bag-map))]
  ;; (when (not= (str desired-bag) ":shiny-gold-bag")
  ;;   (println bag-map)
  ;;   (println desired-bag))
  ;; (println (re-find (re-pattern desired-bag) (apply str bag-map)))
  ;; (println)
  ;; (println (vals bag-map))
  ;; (println (keys bag-map))
  (if (re-find (re-pattern desired-bag) (apply str (vals bag-map)))
    ;;(list (first (keys bag-map)))
    (keys bag-map)
    nil))

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
                    ;;  (remove nil? (list (get-containing-bags bag-map desired-bag)))
                     (get-containing-bags bag-map desired-bag)
                     ))))
    ))

;; (defn final-find-stuff [full-bag-list bags]
;;   (let [containing-bags (remove empty? (remove nil? (map #(apply str (get-all-possible-containers % bags)) full-bag-list)))]
;;     (if (empty? containing-bags)
;;       nil
;;       (final-find-stuff full-bag-list containing-bags))))

(defn final-find-stuff-loop [full-bag-list bags]
  (loop [containing-bags bags
         final-list []]
    (if (empty? containing-bags)
      final-list
      (recur (remove empty? (remove nil? (map #(apply str (get-all-possible-containers % containing-bags)) full-bag-list)))
             (into final-list
                   containing-bags)
             )
      )
  ;; (let [containing-bags (remove empty? (remove nil? (map #(apply str (get-all-possible-containers % bags)) full-bag-list)))]
  ;;   (if (empty? containing-bags)
  ;;     nil
  ;;     (final-find-stuff full-bag-list containing-bags)))
    
    ))

(defn aoc-day7-p1 []
  (let [colored-bag-rules    (slurp colored-bag-rules-file)
        modified-bc (map #(str/replace % #"\sno\s" " 0 ") (str/split-lines colored-bag-rules))
        full-bag-list (map get-map-entry-no-values modified-bc)
        shiny-gold-containers (remove nil? (map #(get-containing-bags % ":shiny-gold-bag") full-bag-list))]
    (map #(str (first %)) shiny-gold-containers)
    ;; (doseq [x full-bag-list]
    ;;   (spit "output.txt" (prn-str x) :append true))
    
    ;; (set (final-find-stuff-loop full-bag-list (list ":shiny-gold-bag")))
    ;; (count (set (final-find-stuff-loop full-bag-list (list ":shiny-gold-bag"))))
    ;; (final-find-stuff full-bag-list (list ":shiny-gold-bag"))
    ;; (println full-bag-list)
    ;; (map #(get-containing-bags % ":shiny-gold-bag") full-bag-list)
    ;; (map #(re-find #"light-teal-bag" (apply str %)) full-bag-list)
    ;; (remove nil? (map #(get-containing-bags % ":shiny-gold-bag") full-bag-list))
    ;; (get-containing-bags (clojure.edn/read-string "{:mirrored-gold-bag (:light-teal-bag)}") ":light-teal-bag")
    ;; (apply str (get-all-possible-containers (first full-bag-list) (str/split ":shiny-gold-bag :light-teal-bag" #" ")))
    ;; (remove empty? (remove nil? (map #(apply str (get-all-possible-containers % (str/split ":mirrored-white-bag :light-teal-bag" #" "))) full-bag-list)))
    
    ;; "((:plaid-fuchsia-bag) (:posh-purple-bag) (:light-crimson-bag) (:posh-beige-bag) (:pale-indigo-bag))"
    )
  )
