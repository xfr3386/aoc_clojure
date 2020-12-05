(ns playground
  (:require [clojure.string :as str]))

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