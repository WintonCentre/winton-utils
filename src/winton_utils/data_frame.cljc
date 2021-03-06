(ns winton-utils.data-frame)

(defn map-of-vs->v-of-maps
  "Transpose a map of vectors to a vector of maps.
  Resulting vector will be truncated to the length of the shortest input vector.
  e.g. {:a [0 1 2] :b [10 11 12]} -> [{:a 0 :b 10} {:a 1 :b 11} {:a 2 :b 12}]"
  [k-vs]
  (cond
    (seq k-vs)
    (mapv (fn [vs]
            (into {} (map-indexed (fn [k v] [(nth (keys k-vs) k) v]) vs)))
          (apply map vector (vals k-vs)))
    (nil? k-vs)
    nil

    :else
    []))


(defn v-of-maps->map-of-vs
  "Transpose a vector of maps to a map of vectors.
  Resulting vector will be truncated to the length of the shortest input vector.
  e.g. [{:a 0 :b 10} {:a 1 :b 11} {:a 2 :b 12}] -> {:a [0 1 2] :b [10 11 12]}"
  [ms]
  (when ms
    (let [ks (into #{} (mapcat keys ms))
          vs (if (seq ks) (mapv (apply juxt ks) ms) [])]
      (into {} (map-indexed
                 (fn [i k] [k (map (fn [val] (val i)) vs)])
                 ks))
      ))
  )



(comment
  ;; wip

  (defn step [vms [k vs]]
    (map-indexed
      (fn [i m]
        (update m k (fn [old-v] (into [] (conj old-v (k m))))))
      vms))

  (comment
    (step [] [:a [1 2 3]])
    ;=> ({:a [1]} {:a [2]} {:a [3]})

    (step {} [:a [1 2 3]])

    (reduce step {} [[:a [1 2 3 4]] [:b [2 3 4 5]]])

    (v-of-maps->map-of-vs {:a [1 2 3 4] :b [2 3 4 5]})
    ;=> [{:a 1 :b 2} {:a 2 :b 3} {:a 3 :b 4} {:a 4 :b 5}]

    )

  )


;;
;; Implement a set of functions which can be used to make transducers that operate on data-frames.
;;
;; In this context a data-frame
;; is a rectangular table of data in columns where each column is identified by a keyword.
;;
;; Example 1:
;; (def df {:a [1 2 3] :b [4 5 6] :c [7 8 9] :d [10 11 12]})
;;
;; Example 2:
;; A spreadsheet with keywords for column names
;;


(defn cell-apply [f]
  (fn [[k vs]] [k (map f vs)]))

(comment
  (into {} (map (cell-apply inc)) {:a (range 10) :b (range 5 15)})
  ;=> {:a (1 2 3 4 5 6 7 8 9 10), :b (6 7 8 9 10 11 12 13 14 15)}
  )

(defn cell-update [f]
  "update a cell at position k index with old value to a new value
  given by (f k index old)"
  (fn [[k vs]]
    (let [g (partial f k)]                                  ;(fn [index old] (f k index old))
      [k (map-indexed g vs)])))

(comment
  (into {}
        (map (cell-update (fn [k index old] (str k "-" index "-" old))))
        {:a [1 2 3] :b [4 5 6]})
  ;=> {:a (":a-0-1" ":a-1-2" ":a-2-3"), :b (":b-0-4" ":b-1-5" ":b-2-6")}
  )

(defn cell-sums
  [[k vs]]
  [k (reductions + vs)])

(comment
  (into {} (map cell-sums) '([:a [1 2 3]] [:b [4 5 6]] [:c [7 8 9]] [:d [10 11 12]]))
  ;=> {:a (1 3 6), :b (4 9 15), :c (7 15 24), :d (10 21 33)}
  )

(defn cell-diffs
  [initial]
  (fn [[k vs]]
    [k (map (fn [[a b]] (- b a)) (partition 2 1 (cons initial vs)))]))

(comment
  (into {} (map (diff-cells 0)) '([:a [0 1 2 3]] [:b [1 6 5 4]] [:c [3 3 3 4]] [:d [0 -1 -2]]))
  ;=> {:a (0 1 1 1), :b (1 5 -1 -1), :c (3 0 0 1), :d (0 -1 -1)}

  (into {} (map (diff-cells 1)) '([:a [0 1 2 3]] [:b [1 6 5 4]] [:c [3 3 3 4]] [:d [0 -1 -2]]))
  ;=> {:a (-1 1 1 1), :b (0 5 -1 -1), :c (2 0 0 1), :d (-1 -1 -1)}
  )

(defn cell-binary
  "Apply a binary function to merge cells from the input dataframe with cells from df"
  [f df]
  (fn [[k vs]] [k (map (completing f) (k df) vs)]))


(defn cell-binary-seq
  "Apply a binary function to merge cells from the input dataframe with cells from a seq"
  [f cs]
  (fn [[k vs]] [k (map (completing f) cs vs)]))

(comment
  (into {}
        (map (cell-binary + {:a (range 10 20) :b (range 10)}))
        {:a (range -10 -20 -1) :b (range 0 -10 -1)})
  ; => {:a (0 0 0 0 0 0 0 0 0 0), :b (0 0 0 0 0 0 0 0 0 0)}
  )


(comment

  ;; compose transducers
  (def xf (comp
            (map (cell-apply inc))
            (map cell-sums)
            (map (cell-diffs 0))
            (map (cell-apply dec))
            ))
  (into {} xf {:a (range 10) :b (range 5 15)})
  ; {:a (0 1 2 3 4 5 6 7 8 9), :b (5 6 7 8 9 10 11 12 13 14)}

  (def a (transduce xf conj {:a (range 10) :b (range 5 15)}))
  (def r (eduction xf {:a (range 10) :b (range 5 15)}))

  ;; The mapping transducer (mapping f) is equivalent to (map f)
  ;; https://stackoverflow.com/questions/32822207/eduction-vs-transducer-composition
  (defn mapping
    ([f]
     (fn [rf]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result input]
          (rf result (f input)))))))

  ;; experimenting with eduction

  )

