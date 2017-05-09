package org.android.securityguard.black.db.dao;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import org.android.securityguard.black.entity.BlackContactInfo;

import java.util.List;
import java.util.Random;

/**
 * Created by Hepsilion on 2017/5/8.
 */
public class TestBlackNumberDao extends AndroidTestCase{
    private Context context;

    @Override
    protected void setUp() throws Exception {
        context=getContext();
        super.setUp();
    }

    /**
     * 测试添加
     * @throws Exception
     */
    public void testAdd() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        Random random=new Random(8979);
        for(long i=0; i<30; i++){
            BlackContactInfo contactInfo=new BlackContactInfo();
            contactInfo.phoneNumber=13500000000L+i+"";
            contactInfo.contactName="zhang"+i;
            contactInfo.mode=random.nextInt(3)+1;
            dao.add(contactInfo);
        }
    }

    /**
     * 测试删除
     * @throws Exception
     */
    public void testDelete() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        BlackContactInfo contactInfo=new BlackContactInfo();
        for(long i=0; i<30; i++){
            contactInfo.phoneNumber=13500000000L+i+"";
            dao.delete(contactInfo);
        }
    }

    /**
     * 测试分页查询
     */
    public void testGetPageBlackNumber() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        List<BlackContactInfo> list=dao.getPageBlackNumber(2, 5);
        for(int i=0; i<list.size(); i++){
            Log.i("TestBlackNumberDao", list.get(i).phoneNumber);
        }
    }

    /**
     * 测试根据号码查询黑名单信息
     * @throws Exception
     */
    public void testGetBlackContactMode() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        int mode=dao.getBlackContactMode(135000000008L+"");
        Log.i("TestBlackNumberDao", mode+"");
    }

    /**
     * 测试数据总条数
     * @throws Exception
     */
    public void testGetTotalNumber() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        int total=dao.getTotalNumber();
        Log.i("TestBlackNumberDao", "数据总条数： "+total);
    }

    /**
     * 测试号码是否在数据库中
     * @throws Exception
     */
    public void testIsNumberExist() throws Exception{
        BlackNumberDao dao=new BlackNumberDao(context);
        boolean isExist=dao.isBlackNumber(13500000008L+"");
        if(isExist){
            Log.i("TestBlackNumberDao", "该号码在数据库中");
        }else{
            Log.i("TestBlackNumberDao", "该号码不在数据库中");
        }
    }
}
