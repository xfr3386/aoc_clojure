(ns playground
  (:require [clojure.string :as str])
  (:require [clojure.edn :as edn])
  (:require [clojure.math.numeric-tower :as math]))

; print 0, -1, -2, -3, -4
(loop [i 0]
  (when (> i -5)
    (println i)
    (recur (dec i))
    ))

; sum of 10+9+8+7...
(loop [sum 0 cnt 10]
  (if (= cnt 0)
    sum
    (recur (+ cnt sum) (dec cnt))
    ))

; destructuring
(def names ["Bob" "Joe" "Sam" "Ned" "Homer" "Bart"])
(let [[item1 & remaining] names] ; item1 = Bob, remaining = the rest
      (println item1)
      (apply println remaining))

(let [sample "ecl:grn
cid:315 iyr:2012 hgt:192cm eyr:2023 pid:873355140 byr:1925 hcl:#cb2c03"]
  (println sample)
  (str/replace sample "\r\n" ""))

(str/split "1234abc" #"(\d[a-zA-Z])")

(def birth-year 2027)
(and (nil? birth-year) (<= 1920 (Integer/parseInt birth-year) 2002))

(re-find #"\d+" "183cm")
(re-find #"\p{Alpha}+" "183cm")

(def rows (range 128))
(def seats (range 8))
;(def search-term "FBFBBFF")
;(println (first search-term))
;(= (subs search-term 0 1) "F")
(println rows)
; if F
(first (split-at (/ (- (count rows) 1) 2) rows))
; if B
(rest (split-at (/ (- (count rows) 1) 2) rows))
(first (partition (/ (count rows) 2) rows))
(last (partition (/ (count rows) 2) rows))
(rest "FBFBBFF")

(defn find-row [search-term remaining-seats comparator]
  (let [two-halves (partition (/ (count remaining-seats) 2) remaining-seats)]
    (if (= (count remaining-seats) 1)
      (println remaining-seats)
      (if (= (subs search-term 0 1) comparator)
        (do 
          (println (first search-term))
          (println (first two-halves))
          (find-row (subs search-term 1) (first two-halves) comparator))
        (do
          (println (first search-term))
          (println (last two-halves))
          (find-row (subs search-term 1) (last two-halves) comparator))
        ))))
(find-row "FBFBBFF" rows "F")
(find-row "RLR" seats "L")
; FBFBBFF
; 0-127
; want 0-63; split-at (127-1)/2
(defn find-position [search-term remaining-values comparator]
  (let [two-halves (partition (/ (count remaining-values) 2) remaining-values)]
    (if (= (count remaining-values) 1)
      (first remaining-values)
      (if (= (subs search-term 0 1) comparator)
        (find-position (subs search-term 1) (first two-halves) comparator)
        (find-position (subs search-term 1) (last two-halves) comparator)))))

(find-position "FBFBBFB" rows "F")
(find-position "RRL" seats "L")

(mod 101 2)

;; a map with a term that contains a sub-map
(def bag-test-map {:gold {:dull-green 3 :muted-green 1 :shiny-orange 2} :fuschia {:dim-blue 3 :dim-red 1 :dim-orange 2}})
(println bag-test-map)

;; to get an item in the submap, get-in with the m
(get-in bag-test-map [:gold :dull-green])

;; dim red bags contain 1 mirrored olive bag, 1 plaid violet bag.
;; {:dim-red {:mirrored-olive-bag 1 :plaid-violet-bag 1 :plaid-violet-bag 1}}
;; posh salmon bags contain 1 shiny brown bag, 2 dark red bags, 3 drab gold bags. ;; NOTICE: bag vs bags
;; {:posh-salmon {:shiny-brown 1 :dark-red 2 :drab-gold 3}}
;; posh salmon bags contain 1 shiny brown bag, 2 dark red bags, 3 drab gold bags.
(def bag-test-string (str/replace "posh salmon bags contain 1 shiny brown bag, 2 dark red bags, 3 drab gold bags." #"bags" "bag"))
;; remove contain, separate main from subs with #, replace trailing . with ,
(def parse-bag-test-string (str/replace bag-test-string #"^(.* bag)\scontain\s(.*)(.)" "$1#$2, "))
(println parse-bag-test-string)

;; GET MAIN BAG NAME
;; get the main bag string, with spaces replaced as -
(def main-bag (str/replace (str/replace parse-bag-test-string #"(.*)#.*" "$1") #" " "-"))
(println main-bag)

;; GENERATE SUB-BAG MAP WITH COUNT AS VALUE
;; get sub-bag strings in the form of #-remaining-name-words
(def containing-seq (map #(str/replace % #"(\w) " "$1-") (re-seq #"\d[\w|\s]+" parse-bag-test-string)))
(println containing-seq)
;; get map form string of sub-bags as ":sub-bag count"
(def sub-bags-with-values (map #(str/replace % #"(\d)-(.*)" ":$2 $1") containing-seq))
(println sub-bags-with-values)
;; combine main bag and sub bag into single map form string, convert to map
(def bag-and-containers-values-hash (clojure.edn/read-string (str "{:" main-bag " {" (str/join " " sub-bags-with-values) "}}")))
(println bag-and-containers-values-hash)

(map #(str %) (get bag-and-containers-values-hash (keyword main-bag)))

;; GENERATE SUB-BAG LIST, DROP VALUES
;; (re-find #"\d[\w|\s]+" parse-bag-test-string)
;; (defn extract-group [n] (fn [group] (group n)))
(def sub-bags (map #(str/replace % #"\d-([\w|-]+)" "$1") containing-seq))
(println sub-bags)
(def bag-and-containers-hash (clojure.edn/read-string (str "{:" main-bag " (" (str/join " " sub-bags) ")}")))
(println bag-and-containers-hash)

(def test-bag-1 (edn/read-string "{:posh-salmon-bag {:shiny-brown-bag 1, :dark-red-bags 2, :drab-gold-bags 3} :shiny-brown-bag {:dark-red-bags 2, :drab-gold-bags 3} :dark-red-bags {:drab-gold-bags 3}}"))
(get test-bag-1 :posh-salmon-bag)
(get test-bag-1 :shiny-brown-bag)
(get-in test-bag-1 [:posh-salmon-bag :shiny-brown-bag])
(select-keys test-bag-1 [:posh-salmon-bag])

(def sub-bag-test (str "(drab-silver-bag dim-coral-bag drab-silver-bag dim-salmon-bag)"))
(re-find #"dim-coral-bag" sub-bag-test)
(into () ":shiny-gold-bag :vibrant-yellow-bag")

(split-at 4 [1 2 3 4 5])

(subvec [1 2 3 4 5 6 7] 0)
(nth [1 2 3 4 5 6 7] 0)

(def diffs [3 1 1 1 1 3 3 1 1 1 1 3 1 1 1 1 3 1 1 3 1 3 1 1 1 1 3 1 1 1 1 3 1 1 1 3 1 1 1 3 3 1 3 1 1 1 1 3 1 1 1 1 3 1 1 1 1 3 3 1 1 3 3 3 3 1 1 1 1 3 1 1 1 1 3 1 1 1 1 3 1 1 1 1 3 1 1 1 3 1 3 1 1 1])
;; (def diffs [3 1 3 1 1 3 1 1 1 3 1])
(def separated-diffs (partition-by #(= 1 %) diffs))
(println separated-diffs)
(loop [index 0
       two-groups 0
       three-groups 0
       four-groups 0]
  (if (= index (count separated-diffs))
    (* (math/expt 2 two-groups) (math/expt 4 three-groups) (math/expt 7 four-groups))
    (let [current-group (nth separated-diffs index)]
      (println current-group " count " (count current-group))
      (if (and (some #(= 1 %) current-group) (> (count current-group) 1))
        (if (= (count current-group) 2)
          (recur (inc index) (inc two-groups) three-groups four-groups)
          (if (= (count current-group) 3)
            (recur (inc index) two-groups (inc three-groups) four-groups)
            (recur (inc index) two-groups three-groups (inc four-groups))))
        (recur (inc index) two-groups three-groups four-groups)))))

(def test-seats "#.#L.L#.###LLL#LL.L#L.#.L..#..#L##.##.L##.#L.LL.LL#.#L#L#.##..L.L.....#L#L##L#L##.LLLLLL.L#.#L#L#.##")
(count (filter #(= "#" (str %)) test-seats))
(frequencies test-seats)