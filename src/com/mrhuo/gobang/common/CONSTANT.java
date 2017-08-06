/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.common;

import com.mrhuo.gobang.bean.GameData;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * 常量类
 */
public final class CONSTANT {
    public static final Color defaultChessBoradColor = new Color(236, 207, 105);
    public static final int gridSize = 30;
    public static final int offsetSizeX = 90;
    public static final int offsetSizeY = 40;
    public static final int serverPort = 9988;
    public static final String serverAddress = "127.0.0.1";
    /**
     * 姓
     */
    private static final String[] USER_FIRST_NAME =
            (
                    "赵,钱,孙,李,周,吴,郑,王,冯,陈,褚,卫,蒋," +
                            "沈,韩,杨,朱,秦,尤,许,何,吕,施,张,孔,曹,严,华,金,魏,陶,姜,戚,谢,邹,喻," +
                            "柏,水,窦,章,云,苏,潘,葛,奚,范,彭,郎,鲁,韦,昌,马,苗,凤,花,方,俞,任,袁,柳," +
                            "丰,鲍,史,唐,费,廉,岑,薛,雷,贺,倪,汤,滕,殷,罗,毕,郝,邬,安,常,乐,于,时," +
                            "傅,皮,卞,齐,康,伍,余,元,卜,顾,孟,平,黄,和,穆,萧,尹,姚,邵,湛,汪,祁,毛," +
                            "禹,狄,米,贝,明,臧,计,伏,成,戴,谈,宋,茅,庞,熊,纪,舒,屈,项,祝,董,梁,杜," +
                            "阮,蓝,闵,席,季,麻,强,贾,路,娄,危,江,童,颜,郭,梅,盛,林,刁,钟,徐,丘,骆,高," +
                            "夏,蔡,田,樊,胡,凌,霍,虞,万,支,柯,昝,管,卢,莫,经,房,裘,缪,干,解,应,宗,丁," +
                            "宣,贲,邓,郁,单,杭,洪,包,诸,左,石,崔,吉,钮,龚,程,嵇,邢,滑,裴,陆,荣,翁,荀," +
                            "羊,於,惠,甄,麴,家,封,芮,羿,储,靳,汲,邴,糜,松,井,段,富,巫,乌,焦,巴,弓,牧," +
                            "隗,山,谷,车,侯,宓,蓬,全,郗,班,仰,秋,仲,伊,宫,宁,仇,栾,暴,甘,钭,厉,戌,祖," +
                            "武,符,刘,景,詹,束,龙,叶,幸,司,韶,郜,黎,蓟,薄,印,宿,白,怀,蒲,邰,从,鄂,索," +
                            "咸,籍,赖,卓,蔺,屠,蒙,池,乔,阴,郁,胥,能,苍,双,闻,莘,党,翟,谭,贡,劳,逢,姬," +
                            "申,扶,堵,冉,宰,郦,雍,郤,璩,桑,桂,濮,牛,寿,通,边,扈,燕,冀,郏,浦,尚,农,温," +
                            "别,庄,晏,柴,瞿,阎,充,慕,连,茹,习,宦,艾,鱼,容,向,古,易,慎,戈,廖,庾,终,暨," +
                            "居,衡,步,都,耿,满,弘,匡,国,文,寇,广,禄,阙,东,欧,殳,沃,利,蔚,越,菱,隆,师," +
                            "巩,厍,聂,晃,勾,敖,融,冷,訾,辛,阚,那,简,饶,空,曾,毋,沙,乜,养,鞠,须,丰,巢," +
                            "关,蒯,相,查,后,荆,红,游,竺,权,逯,盖,益,桓,公,万俟,司马,上官,欧阳,夏侯," +
                            "诸葛,闻人,东方,赫连,皇甫,尉迟,公羊,澹台,公冶,宗政,濮阳,淳于,单于,太叔," +
                            "申屠,公孙,仲孙,轩辕,令狐,钟离,宇文,长孙,慕容,司徒,司空"
            ).split(",");
    /**
     * 名
     */
    private static final String[] USER_LAST_NAME =
            (
                    "努,迪,立,林,维,吐,丽,新,涛,米,亚,克,湘,明," +
                            "白,玉,代,孜,霖,霞,加,永,卿,约,小,刚,光,峰,春,基,木,国,娜,晓,兰,阿,伟,英,元," +
                            "音,拉,亮,玲,木,兴,成,尔,远,东,华,旭,迪,吉,高,翠,莉,云,华,军,荣,柱,科,生,昊," +
                            "耀,汤,胜,坚,仁,学,荣,延,成,庆,音,初,杰,宪,雄,久,培,祥,胜,梅,顺,涛,西,库,康," +
                            "温,校,信,志,图,艾,赛,潘,多,振,伟,继,福,柯,雷,田,也,勇,乾,其,买,姚,杜,关,陈," +
                            "静,宁,春,马,德,水,梦,晶,精,瑶,朗,语,日,月,星,河,飘,渺,星,空,如,萍,棕,影,南,北"
            ).split(",");

    /**
     * 随机获得一个用户姓名
     *
     * @return
     */
    public static String randomName() {
        Random random = new Random();
        return (USER_FIRST_NAME[random.nextInt(USER_FIRST_NAME.length)] +
                USER_LAST_NAME[random.nextInt(USER_LAST_NAME.length)]).trim();
    }

    /**
     * 获取资源图片
     *
     * @param imageName
     * @return
     */
    public static Image getImage(String imageName) {
        ImageIcon imageIcon = getImageIcon(imageName);
        if (imageIcon != null) {
            return imageIcon.getImage();
        }
        return null;
    }

    /**
     * 获取资源图片 ImageIcon
     *
     * @param imageName
     * @return
     */
    public static ImageIcon getImageIcon(String imageName) {
        InputStream inputStream = CONSTANT.class.getClassLoader().getResourceAsStream(imageName + ".png");
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return new ImageIcon(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向服务器 OutputStream 中写入数据
     *
     * @param outputStream
     * @param gameData
     * @throws Exception
     */
    public static void sendData(OutputStream outputStream, GameData gameData) throws Exception {
        synchronized (outputStream){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gameData);
            objectOutputStream.flush();
        }
    }

    /**
     * 从服务器的流中读取数据
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static GameData receiveData(InputStream inputStream) throws Exception {
        synchronized (inputStream) {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (GameData) objectInputStream.readObject();
        }
    }

    /**
     * 给用户弹出信息
     *
     * @param msg
     */
    public static void alertUser(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * 判断SOCKET是否已经连接，可用
     * @param socket
     * @return
     */
    public static boolean isSocketConnected(Socket socket) {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}