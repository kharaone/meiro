(ns meiro.kruskal-test
  (:require [clojure.test :refer :all]
            [meiro.kruskal :refer :all]))


(deftest edges-test
  (testing "Initialize all the edges in a grid."
    (is (= (+ (* 4 (dec 5)) (* (dec 4) 5))
           (count (#'meiro.kruskal/all-edges 4 5))))
    (is (= (- (* 2 15 6) 15 6)
           (count (#'meiro.kruskal/all-edges 15 6))))))


(deftest init-forests-test
  (testing "Forest initialization for a grid."
    (is (= (* 5 4)
           (count (init-forests 5 4))))
    (is (= (* 6 15)
           (count (init-forests 6 15))))))


(deftest find-forest-test
  (testing "Able to find forests by position."
    (let [forests (#'meiro.kruskal/init-forests 6 15)]
      (is (= {:nodes #{[4 13]} :edges []}
             (#'meiro.kruskal/find-forest forests [4 13])))
      (is (nil?
            (#'meiro.kruskal/find-forest forests [4 15]))))))


(deftest merge-forests-test
  (testing "Merge two forests with a shared edge."
    (is (= {:nodes #{[2 4] [2 5]} :edges [[[2 4] [2 5]]]}
           (#'meiro.kruskal/merge-forests
             {:nodes #{[2 4]} :edges []}
             {:nodes #{[2 5]} :edges []}
             [[2 4] [2 5]])))))


(deftest create-test
  (testing "Creating a maze using Kruskal's Algorithm."
    (is (= (dec (* 8 12))
           (count (create 8 12)))))
  (testing "Ensure all cells are linked."
    (is (every?
          #(not-any? empty? %)
          (edges-to-grid (create 10 12) 10 12)))))


(deftest weave-test
  (let [forests (init-forests 3 3)]
    (testing "Can add weaves to forests for a Kruskal's maze."
      (is (= [[[0 1] [2 1]]]
             (-> forests
                 (weave [1 1] :horizontal)
                 (#'meiro.kruskal/find-forest [0 1])
                 :edges)))
      (is (= [[[1 0] [1 1]] [[1 1] [1 2]]]
             (-> forests
                 (weave [1 1] :horizontal)
                 (#'meiro.kruskal/find-forest [1 1])
                 :edges)))
      (is (= [[[0 1] [1 1]] [[1 1] [2 1]]]
             (-> forests
                 (weave [1 1] :vertical)
                 (#'meiro.kruskal/find-forest [1 1])
                 :edges)))
      (is (= [[[1 0] [1 2]]]
             (-> forests
                 (weave [1 1] :vertical)
                 (#'meiro.kruskal/find-forest [1 0])
                 :edges))))
    (testing "Invalid requests return original forests."
      (is (= forests
             (weave forests [0 0])))
      (is (= forests
             (weave forests [0 1]))))))
