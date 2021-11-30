# Mesh and BVH refactor
* Simplify how meshes are handled. Instead of a cumbersome SoA pattern, model them as lists of Triangles, which will contain vertexes/normals/texture coordinates and handle intersection. Get rid of the WorldObject interface and make everything a SimpleObject, which is now just called Object3D.
* Make BVHs capable of containing arbitrary WorldObjects so they can also be reused for the scene-wide BVH.
* Fix bug: WorldObject#getBoundingBox() does not take into account the applied transformation.

However, this presents a bit of a challenge. If BVHs contain WorldObjects, triangles have to be WorldObjects, and instancing breaks since BVHs can no longer be reused between different objects with different materials. Even if we give triangles material IDs, we can't look up the correct material at runtime since there's no way to communicate the materials map to use. If BVHs contain Shapes, it isn't usable for the scene-wide BVH since there's no coupling between shape and material.

Clearly, bigger organizational changes are necessary. Begrudgingly, I also cannot merge WorldObject and SimpleObject, because that would forcefully couple one shape to one material.

Maybe we can abuse Hit's polymorphism to make this work. I've been doing my best to avoid this so far because personally I find that one's intentions can quickly be corrupted in the hands of such power.

OK, potential fix that should have been the first line of thought I pursued. BVHs will contain WorldObjects, and it can be extended to create a special mesh BVH that communicates the material map. Let's code it and pray it doesn't run slower.

I think this also means I can eliminate MeshHit. That's good; it's a rather ugly and frankly hacky solution.

|trial   |bvh build time (ms) |render time (ms) |
|--------|--------------------|-----------------|
|original|15309               |24895            |

# Fix light sampling
* Separate 

# Implement better solution to participating media
* This will apply to spheres too

# Stop using Vector for everything
* Point2D for texture coordinates
* Spectrum (?) for radiances

# Adopt a more rigorous approach to tonemapping
* Also, export raw radiances

# Add a camera that outputs a sphere map

# Add importance sampling for environment maps

# Multiple importance sampling

# Consolidate code that makes the normals returned by Shape#raytrace() face the ray origin into one step afterwards

# Texture mapping refactors
* Make texture mapping more configurable for analytic shapes. Maybe this can be accomplished by transforming texture coordinates instead of changing how they're generated. Probably not
* Generate tangent vectors instead of just using NULL.

# Normal mapping

# Use chi-square test to compare renders with a reference

# Write tests to ensure BRDFs are energy-conserving

# Write tests to make sure samplers and their generated PDFs actually match up

# Implement more materials

# Make texture wrapping mode configurable

# Bidirectional light transport