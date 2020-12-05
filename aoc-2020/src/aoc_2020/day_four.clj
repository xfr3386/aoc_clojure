(ns aoc-2020.day-four
  (:require [clojure.string :as str])
  (:require [clojure.edn :as edn]))

(def passport-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/passports.txt")

(defn get-valid-passport [passport-to-check]
  (if (nil? (get passport-to-check :cid))
    (if (= (count passport-to-check) 7)
      passport-to-check
      nil)
    (if (= (count passport-to-check) 8)
      passport-to-check
      nil)
    ))

(defn aoc-day4-p1 []
  (let [file-contents    (slurp passport-file)
        passports (map #(str/replace % #"\r?\n" " ") (str/split file-contents #"\r?\n\r?\n"))
        mapped-passport-strings (map #(str/replace % #"(\w{3}+)(:)([\w\#]+)" "$2$1 \"$3\"") passports)
        mapped-passports (map #(edn/read-string (str "{" % "}")) mapped-passport-strings)]    
    (time (count (remove nil? (reduce (fn [valid-passports valid-passport]
                                        (into valid-passports (set [(get-valid-passport valid-passport)])))
                                      []
                                      mapped-passports))))
    ))