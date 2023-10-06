/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package jassimp;

/**
 * File: AiPostProcessStepsRaw.java
 * Created on 5 июн. 2023 г., 02:19:00
 *
 * @author LWJGL2
 */
public final class AiPostProcessStepsRaw {

    /**
     * Defines the flags for all possible post processing steps.
     * <p>
     * <h5>Enum values:</h5>
     * <p>
     * <ul>
     * <li>{@link #aiProcess_CalcTangentSpace Process_CalcTangentSpace} -
     * Calculates the tangents and bitangents for the imported meshes.
     * <p>
     * <p>
     * Does nothing if a mesh does not have normals. You might want this post processing step to be executed if you plan to use tangent space calculations
     * such as normal mapping applied to the meshes. There's an importer property, {@link #AI_CONFIG_PP_CT_MAX_SMOOTHING_ANGLE}, which allows you to
     * specify a maximum smoothing angle for the algorithm. However, usually you'll want to leave it at the default value.</p>
     * </li>
     * <li>{@link #aiProcess_JoinIdenticalVertices Process_JoinIdenticalVertices} -
     * Identifies and joins identical vertex data sets within all imported meshes.
     * <p>
     * <p>
     * After this step is run, each mesh contains unique vertices, so a vertex may be used by multiple faces. You usually want to use this post processing
     * step. If your application deals with indexed geometry, this step is compulsory or you'll just waste rendering time. <b>If this flag is not
     * specified</b>, no vertices are referenced by more than one face and <b>no index buffer is required</b> for rendering.</p>
     * </li>
     * <li>{@link #aiProcess_MakeLeftHanded Process_MakeLeftHanded} -
     * Converts all the imported data to a left-handed coordinate space.
     * <p>
     * <p>
     * By default the data is returned in a right-handed coordinate space (which OpenGL prefers). In this space, +X points to the right, +Z points towards
     * the viewer, and +Y points upwards. In the DirectX coordinate space +X points to the right, +Y points upwards, and +Z points away from the viewer.</p>
     * <p>
     * <p>
     * You'll probably want to consider this flag if you use Direct3D for rendering. The {@link #aiProcess_ConvertToLeftHanded Process_ConvertToLeftHanded} flag supersedes this setting and
     * bundles all conversions typically required for D3D-based applications.</p>
     * </li>
     * <li>{@link #aiProcess_Triangulate Process_Triangulate} -
     * Triangulates all faces of all meshes.
     * <p>
     * <p>
     * By default the imported mesh data might contain faces with more than 3 indices. For rendering you'll usually want all faces to be triangles. This
     * post processing step splits up faces with more than 3 indices into triangles. Line and point primitives are *not* modified! If you want 'triangles
     * only' with no other kinds of primitives, try the following solution:</p>
     * <p>
     * <ul>
     * <li>Specify both {@link #aiProcess_Triangulate Process_Triangulate} and {@link #aiProcess_SortByPType Process_SortByPType}</li>
     * <li>Ignore all point and line meshes when you process assimp's output</li>
     * </ul>
     * </li>
     * <li>{@link #aiProcess_RemoveComponent Process_RemoveComponent} -
     * Removes some parts of the data structure (animations, materials, light sources, cameras, textures, vertex components).
     * <p>
     * <p>
     * The components to be removed are specified in a separate importer property, {@link #AI_CONFIG_PP_RVC_FLAGS}. This is quite useful if you don't need
     * all parts of the output structure. Vertex colors are rarely used today for example... Calling this step to remove unneeded data from the pipeline
     * as early as possible results in increased performance and a more optimized output data structure. This step is also useful if you want to force
     * Assimp to recompute normals or tangents. The corresponding steps don't recompute them if they're already there (loaded from the source asset). By
     * using this step you can make sure they are NOT there.</p>
     * <p>
     * <p>
     * This flag is a poor one, mainly because its purpose is usually misunderstood. Consider the following case: a 3D model has been exported from a CAD
     * app, and it has per-face vertex colors. Vertex positions can't be shared, thus the {@link #aiProcess_JoinIdenticalVertices Process_JoinIdenticalVertices} step fails to optimize the data
     * because of these nasty little vertex colors. Most apps don't even process them, so it's all for nothing. By using this step, unneeded components
     * are excluded as early as possible thus opening more room for internal optimizations.</p>
     * </li>
     * <li>{@link #aiProcess_GenNormals Process_GenNormals} -
     * Generates normals for all faces of all meshes.
     * <p>
     * <p>
     * This is ignored if normals are already there at the time this flag is evaluated. Model importers try to load them from the source file, so they're
     * usually already there. Face normals are shared between all points of a single face, so a single point can have multiple normals, which forces the
     * library to duplicate vertices in some cases. {@link #aiProcess_JoinIdenticalVertices Process_JoinIdenticalVertices} is *senseless* then.</p>
     * <p>
     * <p>
     * This flag may not be specified together with {@link #aiProcess_GenSmoothNormals Process_GenSmoothNormals}.</p>
     * </li>
     * <li>{@link #aiProcess_GenSmoothNormals Process_GenSmoothNormals} -
     * Generates smooth normals for all vertices in the mesh.
     * <p>
     * <p>
     * This is ignored if normals are already there at the time this flag is evaluated. Model importers try to load them from the source file, so they're
     * usually already there.</p>
     * <p>
     * <p>
     * This flag may not be specified together with {@link #aiProcess_GenNormals Process_GenNormals}. There's a importer property, {@link #AI_CONFIG_PP_GSN_MAX_SMOOTHING_ANGLE} which
     * allows you to specify an angle maximum for the normal smoothing algorithm. Normals exceeding this limit are not smoothed, resulting in a 'hard'
     * seam between two faces. Using a decent angle here (e.g. 80 degrees) results in very good visual appearance.</p>
     * </li>
     * <li>{@link #aiProcess_SplitLargeMeshes Process_SplitLargeMeshes} -
     * Splits large meshes into smaller sub-meshes.
     * <p>
     * <p>
     * This is quite useful for real-time rendering, where the number of triangles which can be maximally processed in a single draw-call is limited by
     * the video driver/hardware. The maximum vertex buffer is usually limited too. Both requirements can be met with this step: you may specify both a
     * triangle and vertex limit for a single mesh.</p>
     * <p>
     * <p>
     * The split limits can (and should!) be set through the {@link #AI_CONFIG_PP_SLM_VERTEX_LIMIT} and {@link #AI_CONFIG_PP_SLM_TRIANGLE_LIMIT} importer
     * properties. The default values are {@link #AI_SLM_DEFAULT_MAX_VERTICES} and {@link #AI_SLM_DEFAULT_MAX_TRIANGLES}.</p>
     * <p>
     * <p>
     * Note that splitting is generally a time-consuming task, but only if there's something to split. The use of this step is recommended for most users.</p>
     * </li>
     * <li>{@link #aiProcess_PreTransformVertices Process_PreTransformVertices} -
     * Removes the node graph and pre-transforms all vertices with the local transformation matrices of their nodes.
     * <p>
     * <p>
     * If the resulting scene can be reduced to a single mesh, with a single material, no lights, and no cameras, then the output scene will contain only
     * a root node (with no children) that references the single mesh. Otherwise, the output scene will be reduced to a root node with a single level of
     * child nodes, each one referencing one mesh, and each mesh referencing one material.</p>
     * <p>
     * <p>
     * In either case, for rendering, you can simply render all meshes in order - you don't need to pay attention to local transformations and the node
     * hierarchy. Animations are removed during this step.</p>
     * <p>
     * <p>
     * This step is intended for applications without a scenegraph. The step CAN cause some problems: if e.g. a mesh of the asset contains normals and
     * another, using the same material index, does not, they will be brought together, but the first meshes's part of the normal list is zeroed. However,
     * these artifacts are rare.</p>
     * <p>
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     * <p>
     * <p>
     * The {@link #AI_CONFIG_PP_PTV_NORMALIZE} configuration property can be set to normalize the scene's spatial dimension to the {@code -1...1}
     * range.</p>
     * </div>
     * </li>
     * <li>{@link #aiProcess_LimitBoneWeights Process_LimitBoneWeights} -
     * Limits the number of bones simultaneously affecting a single vertex to a maximum value.
     * <p>
     * <p>
     * If any vertex is affected by more than the maximum number of bones, the least important vertex weights are removed and the remaining vertex weights
     * are renormalized so that the weights still sum up to 1. The default bone weight limit is 4 (defined as {@link #AI_LBW_MAX_WEIGHTS} in config.h), but
     * you can use the {@link #AI_CONFIG_PP_LBW_MAX_WEIGHTS} importer property to supply your own limit to the post processing step.</p>
     * <p>
     * <p>
     * If you intend to perform the skinning in hardware, this post processing step might be of interest to you.</p>
     * </li>
     * <li>{@link #aiProcess_ValidateDataStructure Process_ValidateDataStructure} -
     * Validates the imported scene data structure. This makes sure that all indices are valid, all animations and bones are linked correctly, all
     * material references are correct .. etc.
     * <p>
     * <p>
     * It is recommended that you capture Assimp's log output if you use this flag, so you can easily find out what's wrong if a file fails the
     * validation. The validator is quite strict and will find *all* inconsistencies in the data structure... It is recommended that plugin developers use
     * it to debug their loaders. There are two types of validation failures:</p>
     * <p>
     * <ul>
     * <li>Error: There's something wrong with the imported data. Further postprocessing is not possible and the data is not usable at all. The import
     * fails. {@link #aiGetErrorString GetErrorString} carries the error message around.</li>
     * <li>Warning: There are some minor issues (e.g. 1000000 animation keyframes with the same time), but further postprocessing and use of the data
     * structure is still safe. Warning details are written to the log file, {@link #AI_SCENE_FLAGS_VALIDATION_WARNING} is set in
     * {@link AIScene}{@code ::mFlags}.</li>
     * </ul>
     * <p>
     * <p>
     * This post-processing step is not time-consuming. Its use is not compulsory, but recommended.</p>
     * </li>
     * <li>{@link #aiProcess_ImproveCacheLocality Process_ImproveCacheLocality} -
     * Reorders triangles for better vertex cache locality.
     * <p>
     * <p>
     * The step tries to improve the ACMR (average post-transform vertex cache miss ratio) for all meshes. The implementation runs in O(n) and is roughly
     * based on the 'tipsify' algorithm (see <a target="_blank" href="http://gfx.cs.princeton.edu/pubs/Sander_2007_%3ETR/tipsy.pdf">this paper</a>).</p>
     * <p>
     * <p>
     * If you intend to render huge models in hardware, this step might be of interest to you. The {@link #AI_CONFIG_PP_ICL_PTCACHE_SIZE} importer property
     * can be used to fine-tune the cache optimization.</p>
     * </li>
     * <li>{@link #aiProcess_RemoveRedundantMaterials Process_RemoveRedundantMaterials} -
     * Searches for redundant/unreferenced materials and removes them.
     * <p>
     * <p>
     * This is especially useful in combination with the {@link #aiProcess_PreTransformVertices Process_PreTransformVertices} and {@link #aiProcess_OptimizeMeshes Process_OptimizeMeshes} flags. Both join small meshes with
     * equal characteristics, but they can't do their work if two meshes have different materials. Because several material settings are lost during
     * Assimp's import filters, (and because many exporters don't check for redundant materials), huge models often have materials which are are defined
     * several times with exactly the same settings.</p>
     * <p>
     * <p>
     * Several material settings not contributing to the final appearance of a surface are ignored in all comparisons (e.g. the material name). So, if
     * you're passing additional information through the content pipeline (probably using *magic* material names), don't specify this flag. Alternatively
     * take a look at the {@link #AI_CONFIG_PP_RRM_EXCLUDE_LIST} importer property.</p>
     * </li>
     * <li>{@link #aiProcess_FixInfacingNormals Process_FixInfacingNormals} -
     * This step tries to determine which meshes have normal vectors that are facing inwards and inverts them.
     * <p>
     * <p>
     * The algorithm is simple but effective: the bounding box of all vertices + their normals is compared against the volume of the bounding box of all
     * vertices without their normals. This works well for most objects, problems might occur with planar surfaces. However, the step tries to filter such
     * cases. The step inverts all in-facing normals. Generally it is recommended to enable this step, although the result is not always correct.</p>
     * </li>
     * <li>{@link #aiProcess_PopulateArmatureData Process_PopulateArmatureData} -
     * This step generically populates {@code aiBone->mArmature} and {@code aiBone->mNode}.
     * <p>
     * <p>
     * The point of these is it saves you later having to calculate these elements. This is useful when handling rest information or skin information. If
     * you have multiple armatures on your models we strongly recommend enabling this. Instead of writing your own multi-root, multi-armature lookups we
     * have done the hard work for you.</p>
     * </li>
     * <li>{@link #aiProcess_SortByPType Process_SortByPType} -
     * This step splits meshes with more than one primitive type in homogeneous sub-meshes.
     * <p>
     * <p>
     * The step is executed after the triangulation step. After the step returns, just one bit is set in {@link AIMesh}{@code ::mPrimitiveTypes}. This is
     * especially useful for real-time rendering where point and line primitives are often ignored or rendered separately. You can use the
     * {@link #AI_CONFIG_PP_SBP_REMOVE} importer property to specify which primitive types you need. This can be used to easily exclude lines and points,
     * which are rarely used, from the import.</p>
     * </li>
     * <li>{@link #aiProcess_FindDegenerates Process_FindDegenerates} -
     * This step searches all meshes for degenerate primitives and converts them to proper lines or points.
     * <p>
     * <p>
     * A face is 'degenerate' if one or more of its points are identical. To have the degenerate stuff not only detected and collapsed but removed, try
     * one of the following procedures:</p>
     * <p>
     * <ol>
     * <li>(if you support lines and points for rendering but don't want the degenerates)
     * <p>
     * <ul>
     * <li>Specify the {@link #aiProcess_FindDegenerates Process_FindDegenerates} flag.</li>
     * <li>Set the {@link #AI_CONFIG_PP_FD_REMOVE} importer property to 1. This will cause the step to remove degenerate triangles from the import as
     * soon as they're detected. They won't pass any further pipeline steps.</li>
     * </ul></li>
     * <li>(if you don't support lines and points at all)
     * <p>
     * <ul>
     * <li>Specify the {@link #aiProcess_FindDegenerates Process_FindDegenerates} flag.</li>
     * <li>Specify the {@link #aiProcess_SortByPType Process_SortByPType} flag. This moves line and point primitives to separate meshes.</li>
     * <li>Set the {@link #AI_CONFIG_PP_SBP_REMOVE} importer property to <code>{@link #aiPrimitiveType_POINT PrimitiveType_POINT} | {@link #aiPrimitiveType_LINE PrimitiveType_LINE}</code> to cause
     * {@link #aiProcess_SortByPType Process_SortByPType} to reject point and line meshes from the scene.</li>
     * </ul></li>
     * </ol>
     * <p>
     * <p>
     * This step also removes very small triangles with a surface area smaller than 10^-6. If you rely on having these small triangles, or notice holes in
     * your model, set the property {@link #AI_CONFIG_PP_FD_CHECKAREA} to {@code false}.</p>
     * <p>
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     * <p>
     * <p>
     * Degenerate polygons are not necessarily evil and that's why they're not removed by default. There are several file formats which don't support
     * lines or points, and some exporters bypass the format specification and write them as degenerate triangles instead.</p></div>
     * </li>
     * <li>{@link #aiProcess_FindInvalidData Process_FindInvalidData} -
     * This step searches all meshes for invalid data, such as zeroed normal vectors or invalid UV coords and removes/fixes them. This is intended to get
     * rid of some common exporter errors.
     * <p>
     * <p>
     * This is especially useful for normals. If they are invalid, and the step recognizes this, they will be removed and can later be recomputed, i.e. by
     * the {@link #aiProcess_GenSmoothNormals Process_GenSmoothNormals} flag.</p>
     * <p>
     * <p>
     * The step will also remove meshes that are infinitely small and reduce animation tracks consisting of hundreds if redundant keys to a single key.
     * The {@link #AI_CONFIG_PP_FID_ANIM_ACCURACY} config property decides the accuracy of the check for duplicate animation tracks.</p>
     * </li>
     * <li>{@link #aiProcess_GenUVCoords Process_GenUVCoords} -
     * This step converts non-UV mappings (such as spherical or cylindrical mapping) to proper texture coordinate channels.
     * <p>
     * <p>
     * Most applications will support UV mapping only, so you will probably want to specify this step in every case. Note that Assimp is not always able
     * to match the original mapping implementation of the 3D app which produced a model perfectly. It's always better to let the modelling app compute
     * the UV channels - 3ds max, Maya, Blender, LightWave, and Modo do this for example.</p>
     * <p>
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     * <p>
     * <p>
     * If this step is not requested, you'll need to process the {@link #_AI_MATKEY_MAPPING_BASE} material property in order to display all assets
     * properly.</p>
     * </div>
     * </li>
     * <li>{@link #aiProcess_TransformUVCoords Process_TransformUVCoords} -
     * This step applies per-texture UV transformations and bakes them into stand-alone vtexture coordinate channels.
     * <p>
     * <p>
     * UV transformations are specified per-texture - see the {@link #_AI_MATKEY_UVTRANSFORM_BASE} material key for more information. This step processes
     * all textures with transformed input UV coordinates and generates a new (pre-transformed) UV channel which replaces the old channel. Most
     * applications won't support UV transformations, so you will probably want to specify this step.</p>
     * <p>
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     * <p>
     * <p>
     * UV transformations are usually implemented in real-time apps by transforming texture coordinates at vertex shader stage with a 3x3 (homogeneous)
     * transformation matrix.</p></div>
     * </li>
     * <li>{@link #aiProcess_FindInstances Process_FindInstances} -
     * This step searches for duplicate meshes and replaces them with references to the first mesh.
     * <p>
     * <p>
     * This step takes a while, so don't use it if speed is a concern. Its main purpose is to workaround the fact that many export file formats don't
     * support instanced meshes, so exporters need to duplicate meshes. This step removes the duplicates again. Please note that Assimp does not currently
     * support per-node material assignment to meshes, which means that identical meshes with different materials are currently *not* joined, although
     * this is planned for future versions.</p>
     * </li>
     * <li>{@link #aiProcess_OptimizeMeshes Process_OptimizeMeshes} -
     * A post-processing step to reduce the number of meshes.
     * <p>
     * <p>
     * This will, in fact, reduce the number of draw calls.</p>
     * <p>
     * <p>
     * This is a very effective optimization and is recommended to be used together with {@link #aiProcess_OptimizeGraph Process_OptimizeGraph}, if possible. The flag is fully compatible
     * with both {@link #aiProcess_SplitLargeMeshes Process_SplitLargeMeshes} and {@link #aiProcess_SortByPType Process_SortByPType}.</p>
     * </li>
     * <li>{@link #aiProcess_OptimizeGraph Process_OptimizeGraph} -
     * A post-processing step to optimize the scene hierarchy.
     * <p>
     * <p>
     * Nodes without animations, bones, lights or cameras assigned are collapsed and joined.</p>
     * <p>
     * <p>
     * Node names can be lost during this step. If you use special 'tag nodes' to pass additional information through your content pipeline, use the
     * {@link #AI_CONFIG_PP_OG_EXCLUDE_LIST} importer property to specify a list of node names you want to be kept. Nodes matching one of the names in this
     * list won't be touched or modified.</p>
     * <p>
     * <p>
     * Use this flag with caution. Most simple files will be collapsed to a single node, so complex hierarchies are usually completely lost. This is not
     * useful for editor environments, but probably a very effective optimization if you just want to get the model data, convert it to your own format,
     * and render it as fast as possible.</p>
     * <p>
     * <p>
     * This flag is designed to be used with {@link #aiProcess_OptimizeMeshes Process_OptimizeMeshes} for best results.</p>
     * <p>
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     * <p>
     * <p>
     * 'Crappy' scenes with thousands of extremely small meshes packed in deeply nested nodes exist for almost all file formats. {@link #aiProcess_OptimizeMeshes Process_OptimizeMeshes}
     * in combination with {@link #aiProcess_OptimizeGraph Process_OptimizeGraph} usually fixes them all and makes them renderable.</p>
     * </div>
     * </li>
     * <li>{@link #aiProcess_FlipUVs Process_FlipUVs} -
     * This step flips all UV coordinates along the y-axis and adjusts material settings and bitangents accordingly.
     * <p>
     * <p>
     * <b>Output UV coordinate system:</b></p>
     * <p>
     * <pre><code>
     * 0y|0y ---------- 1x|0y
     *   |                |
     *   |                |
     *   |                |
     * 0x|1y ---------- 1x|1y</code></pre>
     * <p>
     * <p>
     * You'll probably want to consider this flag if you use Direct3D for rendering. The {@link #aiProcess_ConvertToLeftHanded Process_ConvertToLeftHanded} flag supersedes this setting and
     * bundles all conversions typically required for D3D-based applications.</p>
     * </li>
     * <li>{@link #aiProcess_FlipWindingOrder Process_FlipWindingOrder} -
     * This step adjusts the output face winding order to be CW.
     * <p>
     * <p>
     * The default face winding order is counter clockwise (CCW).</p>
     * <p>
     * <p>
     * <b>Output face order:</b></p>
     * <p>
     * <pre><code>
     *     x2
     *
     *                   x0
     * x1</code></pre>
     * </li>
     * <li>{@link #aiProcess_SplitByBoneCount Process_SplitByBoneCount} -
     * This step splits meshes with many bones into sub-meshes so that each sub-mesh has fewer or as many bones as a given limit.
     * </li>
     * <li>{@link #aiProcess_Debone Process_Debone} -
     * This step removes bones losslessly or according to some threshold.
     * <p>
     * <p>
     * In some cases (i.e. formats that require it) exporters are forced to assign dummy bone weights to otherwise static meshes assigned to animated
     * meshes. Full, weight-based skinning is expensive while animating nodes is extremely cheap, so this step is offered to clean up the data in that
     * regard.</p>
     * <p>
     * <p>
     * Use {@link #AI_CONFIG_PP_DB_THRESHOLD} to control this. Use {@link #AI_CONFIG_PP_DB_ALL_OR_NONE} if you want bones removed if and only if all bones
     * within the scene qualify for removal.</p>
     * </li>
     * <li>{@link #aiProcess_GlobalScale Process_GlobalScale} -
     * This step will perform a global scale of the model.
     * <p>
     * <p>
     * Some importers are providing a mechanism to define a scaling unit for the model. This post processing step can be used to do so. You need to get
     * the global scaling from your importer settings like in FBX. Use the flag {@code AI_CONFIG_GLOBAL_SCALE_FACTOR_KEY} from the global property table
     * to configure this.</p>
     * <p>
     * <p>
     * Use {@link #AI_CONFIG_GLOBAL_SCALE_FACTOR_KEY} to setup the global scaling factor.</p>
     * </li>
     * <li>{@link #aiProcess_EmbedTextures Process_EmbedTextures} -
     * A postprocessing step to embed of textures.
     * <p>
     * <p>
     * This will remove external data dependencies for textures. If a texture's file does not exist at the specified path (due, for instance, to an
     * absolute path generated on another system), it will check if a file with the same name exists at the root folder of the imported model. And if so,
     * it uses that.</p>
     * </li>
     * <li>{@link #aiProcess_ForceGenNormals Process_ForceGenNormals}</li>
     * <li>{@link #aiProcess_DropNormals Process_DropNormals} -
     * Drops normals for all faces of all meshes.
     * <p>
     * <p>
     * This is ignored if no normals are present.</p>
     * <p>
     * <p>
     * Face normals are shared between all points of a single face, so a single point can have multiple normals, which forces the library to duplicate
     * vertices in some cases. {@link #aiProcess_JoinIdenticalVertices Process_JoinIdenticalVertices} is <em>senseless</em> then. This process gives sense back to {@link #aiProcess_JoinIdenticalVertices Process_JoinIdenticalVertices}.</p>
     * </li>
     * <li>{@link #aiProcess_GenBoundingBoxes Process_GenBoundingBoxes}</li>
     * </ul>
     */
    public static final int 
            aiProcess_CalcTangentSpace = 0x1,
            aiProcess_JoinIdenticalVertices = 0x2,
            aiProcess_MakeLeftHanded = 0x4,
            aiProcess_Triangulate = 0x8,
            aiProcess_RemoveComponent = 0x10,
            aiProcess_GenNormals = 0x20,
            aiProcess_GenSmoothNormals = 0x40,
            aiProcess_SplitLargeMeshes = 0x80,
            aiProcess_PreTransformVertices = 0x100,
            aiProcess_LimitBoneWeights = 0x200,
            aiProcess_ValidateDataStructure = 0x400,
            aiProcess_ImproveCacheLocality = 0x800,
            aiProcess_RemoveRedundantMaterials = 0x1000,
            aiProcess_FixInfacingNormals = 0x2000,
            aiProcess_PopulateArmatureData = 0x4000,
            aiProcess_SortByPType = 0x8000,
            aiProcess_FindDegenerates = 0x10000,
            aiProcess_FindInvalidData = 0x20000,
            aiProcess_GenUVCoords = 0x40000,
            aiProcess_TransformUVCoords = 0x80000,
            aiProcess_FindInstances = 0x100000,
            aiProcess_OptimizeMeshes = 0x200000,
            aiProcess_OptimizeGraph = 0x400000,
            aiProcess_FlipUVs = 0x800000,
            aiProcess_FlipWindingOrder = 0x1000000,
            aiProcess_SplitByBoneCount = 0x2000000,
            aiProcess_Debone = 0x4000000,
            aiProcess_GlobalScale = 0x8000000,
            aiProcess_EmbedTextures = 0x10000000,
            aiProcess_ForceGenNormals = 0x20000000,
            aiProcess_DropNormals = 0x40000000,
            aiProcess_GenBoundingBoxes = 0x80000000;
}
