package com.yuqirong.rxnews;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGeneration {

    public static void main(String[] arg0) {

        try {
            Schema schema = new Schema(2, "com.yuqirong.greendao");
            addEntity(schema);
            new DaoGenerator().generateAll(schema, "C:/Users/h/Desktop/RxNews/app/src/main/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addEntity(Schema schema) {
        Entity entity = schema.addEntity("ResultEntity");
        entity.addIdProperty();
        entity.addStringProperty("json");
        entity.addStringProperty("nId");

        Entity entity2 = schema.addEntity("ChannelEntity");
        entity2.addIdProperty();
        entity2.addStringProperty("name");
        entity2.addStringProperty("tId");
        entity2.addBooleanProperty("isSelect");
    }

}
