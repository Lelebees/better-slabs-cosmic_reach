package nl.lelebees.betterslabs.extras;

import finalforeach.cosmicreach.world.blocks.BlockState;

public class LeleUtil {
    public static BlockState changeState(BlockState blockState, String newOrientation) {
        String[] blockStateId = blockState.stringId.split("=");
        StringBuilder stateIdBuilder = new StringBuilder();
        for (String string : blockStateId) {
            if (string.equals(blockStateId[blockStateId.length - 1])) {
                stateIdBuilder.append(newOrientation);
                continue;
            }
            stateIdBuilder.append(string).append("=");
        }

        String saveKey = blockState.getBlockId() + "[" + stateIdBuilder + "]";
        blockState = BlockState.getInstance(saveKey);
        return blockState;
    }
}
