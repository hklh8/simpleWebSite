package com.hklh8.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by GouBo on 2018/1/28.
 */
@Component
public class SystemDataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${data.file}")
    String path;

    @Value("${image.path}")
    String imagePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initImagePath();
        initDataFile();
    }

    private void initImagePath() {
        CreateMultilayerFile(imagePath);
    }

    private void initDataFile() {
        createNewFile(path);
    }

    /**
     * 新建文件
     */
    private void createNewFile(String path) {
        try {
            int lastLength = path.lastIndexOf("/");
            //得到文件夹目录
            String dir = path.substring(0, lastLength);
            if (CreateMultilayerFile(dir)) {
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.createNewFile();
                }
            }
        } catch (Exception e) {
            logger.error("新建文件操作出错: " + e.getMessage());
            e.printStackTrace();
        }


    }

    /**
     * 创建多级文件夹
     */
    private boolean CreateMultilayerFile(String dir) {
        try {
            File dirPath = new File(dir);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
        } catch (Exception e) {
            logger.error("创建多层目录操作出错: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
