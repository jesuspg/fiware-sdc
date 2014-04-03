package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import static java.text.MessageFormat.format;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;

@Service("catalogManagerMongo")
public class CatalogManagerMongoImpl implements CatalogManager {

    @Resource
    protected MongoTemplate mongoTemplate;

    private String eol = System.getProperty("line.separator");

    public void addNode(Node node) {
        try {
            getNode(node.getId());
            mongoTemplate.remove(node);
            mongoTemplate.insert(node);

        } catch (NoSuchElementException ex) {
            mongoTemplate.insert(node);
        }

    }

    public Node getNode(String nodeName) {
        Query searchNodeQuery = new Query(Criteria.where("id").is(nodeName));
        Node savedNode = mongoTemplate.findOne(searchNodeQuery, Node.class);
        if (savedNode == null) {
            throw new NoSuchElementException(format("The node {0} could not be found", nodeName));
        }
        return savedNode;
    }

    public void removeNode(String nodeName) {
        Query searchNodeQuery = new Query(Criteria.where("id").is(nodeName));
        mongoTemplate.remove(searchNodeQuery, Node.class);

    }

    public int getNodeLength() {
        List<Node> nodes = mongoTemplate.findAll(Node.class);
        return nodes.size();
    }

    public String generateManifestStr(String nodeName) {
        StringBuffer sb = new StringBuffer();

        Node node = getNode(nodeName);
        node.setManifestGenerated(true);
        addNode(node);

        sb.append(node.generateFileStr());
        return sb.toString();
    }

    public String generateSiteStr() {

        List<Node> nodeList = mongoTemplate.findAll(Node.class);

        StringBuffer sb = new StringBuffer();

        sb.append("include puppet");
        sb.append(eol);

        for (Node node : nodeList) {
            if (node.isManifestGenerated() && sb.indexOf("import '" + node.getGroupName() + "/*.pp'") == -1) {
                sb.append("import '" + node.getGroupName() + "/*.pp'");
                sb.append(eol);
            }
        }
        return sb.toString();

    }

    public void removeNodesByGroupName(String groupName) {
        Query searchNodeQuery = new Query(Criteria.where("groupName").is(groupName));
        mongoTemplate.remove(searchNodeQuery, Node.class);

    }

    // public void setMongoTemplate(MongoTemplate mongoTemplate) {
    // this.mongoTemplate = mongoTemplate;
    // }

}
