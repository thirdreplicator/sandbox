(ns cylinder.core
  (:use [penumbra opengl]
        [cantor]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn reshape [[x y width height] state]
  (frustum-view 50 (/ (double width) height) 0.1 100)
  (load-identity)
  (translate 0 -0.35 -5.75)
  (light 0
    :position [1 -1 -1 0])
  (fog
    :fog-mode :exp
    :fog-density 0.05
    :fog-start 0
    :fog-end 10
    :fog-color [0 0 0 0])
  state)

(defn mouse-drag [[dx dy] _ button state]
  (assoc state
    :rot-x (+ (:rot-x state) dy)
    :rot-y (+ (:rot-y state) dx)))

(defn cylinder-vertices
  [lod]
  (for [y (range -2.01 2.01 (/ 2.0 lod))]
    (for [theta (range 0 400 (/ 360 lod))]
      (let [y-offset     (vec3 0 y 0)
            circle-point (cartesian (polar3 theta 0))]
        (add circle-point y-offset)))))

(defn draw-cylinder [vertices]
  (material :front-and-back
    :ambient-and-diffuse [0.7 0.7 1 1]
    :specular            [1 1 1 0.5]
    :shininess           1000)
  (doseq [arcs (partition 2 1 vertices)]
    (draw-quad-strip
     (doseq [[[a b] [c d]] (partition 2 1 (map list (first arcs) (second arcs)))]
       (let [u (sub a d)
             v (sub b d)]
         (normal (normalize (cross u v))))
       (vertex a)
       (vertex b)))))

(defn render-cylinder [n]
  (draw-cylinder (cylinder-vertices n)))

(defn init-cylinder []
  (def cylinder (create-display-list (render-cylinder 36))))

(defn display [[delta time] state]
  (rotate (:rot-x state) 1 0 0)
  (rotate (:rot-y state) 0 1 0)
  (clear-color 0 0 0 1)
  (clear)
  (call-display-list cylinder)
  (app/repaint!))

(defn init [state]
  (init-cylinder)
  (app/title! "Cylinder")
  (enable :normalize)
  (enable :depth-test)
;  (enable :cull-face)
  (enable :lighting)
  (enable :light0)
  (enable :fog)
  (shade-model :smooth)
  state)

(defn start []
  (app/start
    {:init init
     :mouse-drag mouse-drag
     :display display
     :reshape reshape } {:rot-x 0, :rot-y 0}))

