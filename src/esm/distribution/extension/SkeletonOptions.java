package esm.distribution.extension;

/**
 * The SkeletonOptions sets a conjunct of properties for {@link esm.distribution.invocation.Skeleton}s connections.
 *
 * @author Pedro Henrique
 */
public class SkeletonOptions {

    /**
     * The Skeleton connection {@link BlockMode}.
     */
    private BlockMode blockMode;

    /**
     * The max number of connection allowed at the same time by the Skeleton.
     */
    private int maxConnections;

    /**
     * Creates the SkeletonOptions with never block connections.
     */
    public SkeletonOptions() {
        blockMode = BlockMode.NEVER_BLOCK;
        maxConnections = Integer.MAX_VALUE;
    }

    /**
     * Creates the SkeletonOptions with the received properties.
     *
     * @param blockMode      the Skeleton {@link BlockMode}
     * @param maxConnections the max number of connections to blocks
     */
    public SkeletonOptions(BlockMode blockMode, int maxConnections) {
        this.blockMode = blockMode;
        if (maxConnections < 1) {
            throw new IllegalArgumentException("The max connections can not be less than 1.");
        }
        this.maxConnections = maxConnections;
    }

    /**
     * Returns the Skeleton {@link BlockMode} option.
     *
     * @return the Skeleton block mode
     */
    public BlockMode getBlockMode() {
        return blockMode;
    }

    /**
     * The max number of connection allowed at the same time by the Skeleton.
     *
     * @return the max number of connections
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * The {@link SkeletonOptions} block mode. Indicates how the Skeleton limit the number of connections.
     */
    public enum BlockMode {

        /**
         * Never blocks any received connection.
         */
        NEVER_BLOCK,

        /**
         * If the Skeleton reaches the max number of connections, the next connections are queued.
         */
        QUEUE_MAX_CONN,

        /**
         * If the Skeleton reaches the max number of connections, the next connections are blocked.
         */
        BLOCK_MAX_CONN,

        /**
         * If the Skeleton reaches the max number of connections, the next connections are forward if other skeletons of
         * the same type are available, otherwise the connections are blocked.
         */
        FORWARD_MAX_CON
    }
}
