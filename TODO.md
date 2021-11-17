# Bugs
* Sphere normals are wrong if you're inside the sphere
  * MAYBE: Implement a general normal correction in Hit instead of re implementing the same code many times for each shape

# Features
* More configurable texture mapping for parametric shapes
  * Azimuth/inclination offset for spheres
  * Texture rotation for planes (allow user to specify basis vector rather than getOrthagonal or at least a rotation)
* Transform system w/ matrices

* texture mapping for cylinders
* various optimizations
* normal mapping
* texture wrap mode
* scene-wide BVH