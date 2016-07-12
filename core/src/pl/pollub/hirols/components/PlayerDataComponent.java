package pl.pollub.hirols.components;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-03-09.
 */
public class PlayerDataComponent implements Component{

    public Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
    public Map<String, Integer> mines = new HashMap<String, Integer>();
    public ArrayList<String> towns = new ArrayList<String>();
}
