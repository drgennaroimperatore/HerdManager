package com.ilri.herdmanager.kmz;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LocationsLoader extends AsyncTask<InputStream, Integer, LocationData>
{
    ArrayList<String> mRegions = new ArrayList<>();
    private HashMap<String, ArrayList<String>> mZonesForRegions = new HashMap<>();
    private HashMap<String, ArrayList<String>> mWordeasForZones = new HashMap<>();
    private HashMap<String, ArrayList<String>> mKebelesForWoredas = new HashMap<>();

    class NodeContainer
    {
        public String Name;
        public NodeList Children;

        public NodeContainer( String name, NodeList children )
        {
            Name = name; Children = children;
        }
    }

    private NodeContainer generateNodeContainer (Node element)
    {
        Element el =(Element)element;
        String nodeName = el.getAttribute("name");
        NodeList children = null;
        if(el.hasChildNodes())
          children = el.getChildNodes();
        return new NodeContainer(nodeName, children);

    }


    @Override
    protected LocationData doInBackground(InputStream... inputStreams)
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStreams[0]);
            NodeList regionNodes = doc.getElementsByTagName("Region");

            for (int r=0; r<regionNodes.getLength(); r++)
            {
                Node regionNode = regionNodes.item(r);
                if(regionNode.getNodeType()!= Node.ELEMENT_NODE)
                    continue;
                NodeContainer regionContainer = generateNodeContainer(regionNodes.item(r));
                NodeList zones = regionContainer.Children;
                ArrayList <String> zonesForRegion = new ArrayList<>();
                mRegions.add(regionContainer.Name);
                if(zones==null)
                    continue;
                else
                if(zones.getLength()==0)
                    continue;
                for (int z=0; z<zones.getLength(); z++)
                {
                    Node zoneNode = zones.item(z);
                    if(zoneNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    NodeContainer zoneContainer = generateNodeContainer(zoneNode);
                    NodeList woredaNodes = zoneContainer.Children;
                    ArrayList<String> woredasForZone = new ArrayList<>();
                    zonesForRegion.add(zoneContainer.Name);
                    for (int w=0; w<woredaNodes.getLength(); w++)
                    {
                        Node woredaNode = woredaNodes.item(w);
                        if(woredaNode.getNodeType()!= Node.ELEMENT_NODE)
                            continue;
                        NodeContainer woredaContainer = generateNodeContainer(woredaNode);
                        NodeList kebeleNodes = woredaContainer.Children;
                        woredasForZone.add(woredaContainer.Name);
                        ArrayList<String> kebelesForWoreda = new ArrayList<>();

                        for( int k=0; k<kebeleNodes.getLength(); k++)
                        {
                            Node kebeleNode = kebeleNodes.item(k);
                            if(kebeleNode.getNodeType()!= Node.ELEMENT_NODE)
                                continue;
                            NodeContainer kebeleContainter = generateNodeContainer(kebeleNode);
                            kebelesForWoreda.add(kebeleContainter.Name);
                        }
                        mKebelesForWoredas.put(woredaContainer.Name,kebelesForWoreda);
                    }
                    mWordeasForZones.put(zoneContainer.Name,woredasForZone);
                }
                mZonesForRegions.put(regionContainer.Name,zonesForRegion);
            }

            LocationData.getInstance().setKebelesForWoredas(mKebelesForWoredas);
            LocationData.getInstance().setWordeasForZones(mWordeasForZones);
            LocationData.getInstance().setZonesForRegions(mZonesForRegions);
            LocationData.getInstance().setRegions(mRegions);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
