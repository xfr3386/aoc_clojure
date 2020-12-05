(ns aoc-2020.day-two
  (:require [clojure.string :as str]))

(def pwd-db "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/pwd_db.txt")

(defn get-valid-entry-p1 [db-entry]
  (let [[range letter-col password] (str/split db-entry #" ")
        [lower upper] (str/split range #"-")
        [letter _] letter-col
        count-of-letter (get (frequencies password) letter)]
    (if (nil? count-of-letter)
      nil
      (if (<= (Long/parseLong lower) count-of-letter (Long/parseLong upper))
        [db-entry]
        nil))))

(defn aoc-day2-p1-reduce "AOC day 2 part 1" []
  (let [file-contents    (slurp pwd-db)
        password-entries (str/split-lines file-contents)]
    (println (count (remove nil? (reduce (fn [valid-entries valid-entry]
                                        (into valid-entries (set [(get-valid-entry-p1 valid-entry)])))
                                      []
                                      password-entries))))))

(defn get-valid-entry-p2 [db-entry]
  (let [[range letter-col password] (str/split db-entry #" ")
        [first-position second-position] (str/split range #"-")
        [letter _] letter-col]
    (if (= (get (frequencies (str/join [(nth password (- (Integer/parseInt first-position) 1)) (nth password (- (Integer/parseInt second-position) 1)) letter])) letter) 2)
      [letter]
      nil)
    ))

(defn aoc-day2-p2-reduce "AOC day 2 part 2" []
  (let [file-contents    (slurp pwd-db)
        password-entries (str/split-lines file-contents)]
    (println (count (remove nil? (reduce (fn [valid-entries valid-entry]
                                        (into valid-entries (set [(get-valid-entry-p2 valid-entry)])))
                                      []
                                      password-entries))))))