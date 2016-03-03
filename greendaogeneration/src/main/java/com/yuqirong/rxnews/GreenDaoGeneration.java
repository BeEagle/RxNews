package com.yuqirong.rxnews;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGeneration {

    public static void main(String[] arg0) {

        try {
            Schema schema = new Schema(1, "com.yuqirong.greendao");
            addEntity(schema);
            new DaoGenerator().generateAll(schema, "C:/Users/h/Desktop/RxNews-master/app/src/main/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addEntity(Schema schema) {
        Entity entity = schema.addEntity("ResultEntity");
        entity.addIdProperty();
        entity.addStringProperty("json");
        entity.addStringProperty("nId");
    }

}
