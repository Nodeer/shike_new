package com.yshow.shike.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.yshow.shike.entity.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.activities.Login_Reg_Activity;
import com.yshow.shike.service.MySKService;

/**
 * 解析Json
 */
public class SKResolveJsonUtil {
    private static SKResolveJsonUtil instance = null;
    private Star_Teacher_Parse teacherParse;
    private SKAcknowledge_list skAcknowledge_list;
    private JSONObject dataobject;

    public static SKResolveJsonUtil getInstance() {
        if (instance == null) { // line 12
            instance = new SKResolveJsonUtil(); // line 13
        }
        return instance;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Context context;
            Intent it;
            switch (msg.what) {
                case 0:
                    LoginManage.getInstance().setStudent(null);
                    context = (Context) msg.obj;
                    it = new Intent(context, Login_Reg_Activity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);
                    Toast.makeText(context, "您的帐号在其他地方登录，您被迫下线，请重新登录。", Toast.LENGTH_SHORT).show();
                    context.stopService(new Intent(context, MySKService.class));
                    break;
                case 1:
                    if (LoginManage.getInstance().getStudent() == null) {
                        return;
                    }
                    LoginManage.getInstance().setStudent(null);
                    context = (Context) msg.obj;
                    it = new Intent(context, Login_Reg_Activity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);
                    Toast.makeText(context, "您的网络发生故障，请重新登录", Toast.LENGTH_SHORT).show();
                    context.stopService(new Intent(context, MySKService.class));
                    break;
            }
        }

        ;
    };

    public boolean resolveIsSuccess(String json, Context context) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int optBoolean = jsonObject.optInt("success");
            int code = jsonObject.optInt("code");
            if (optBoolean == 1) {
                return true;
            } else if (code == 403) {
                Message obtainMessage = handler.obtainMessage(0, context);
                handler.sendMessage(obtainMessage);
                return false;
            } else if (code == 404) {
                Message obtainMessage = handler.obtainMessage(1, context);
                handler.sendMessage(obtainMessage);
                return false;
            } else {
                String error = jsonObject.optString("error");
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public String resolveTakeQuestion(String json, Context context) {
        JSONObject jsonObject = null;
        String error = "";
        try {
            jsonObject = new JSONObject(json);
            error = jsonObject.optString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }

    public boolean resolveIsSuccess(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int optBoolean = jsonObject.optInt("success");
            if (optBoolean == 1) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
    }

    public SKStudent resolveLoginInfo(String json) {
        SKStudent student = new SKStudent();
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int optBoolean = jsonObject.optInt("success");
                if (optBoolean == 1) {
                    jsonObject = jsonObject.getJSONObject("data");
                    student.setaId(jsonObject.optString("aId"));
                    student.setBirthday(jsonObject.optString("birthday"));
                    student.setEmail(jsonObject.optString("email"));
                    student.setGradeId(jsonObject.optString("gradeId"));
                    student.setInfo(jsonObject.optString("info"));
                    student.setMob(jsonObject.optString("mob"));
                    student.setName(jsonObject.optString("name"));
                    student.setNew_messges(jsonObject.optInt("new_messges"));
                    student.setNickname(jsonObject.optString("nickname"));
                    student.setPoints(jsonObject.optString("points"));
                    student.setSchool(jsonObject.optString("school"));
                    student.setSex(jsonObject.optString("sex"));
                    student.setTypes(jsonObject.optString("types"));
                    student.setUid(jsonObject.optString("uid"));
                    student.setIcon(jsonObject.optString("icon"));
                    student.gradePart = jsonObject.optInt("grade_part");
                    return student;
                }
            } catch (JSONException e) {
                return student;
            }
        }
        return student;
    }

    public ArrayList<SKGrade> resolveGrade(String json) {
        ArrayList<SKGrade> sKGrades = new ArrayList<SKGrade>();
        JSONObject jsonSKGrade = null;
        SKGrade sKGrade = null;
        JSONArray jsonArraySKClasses = null;
        SkClasses sKClass = null;
        JSONObject jsonSkClass = null;
        ArrayList<SkClasses> sKClasses = null;
        if (json != null) {
            try {
                JSONObject jo = new JSONObject(json);
                JSONArray jsonArraySKGrads = jo.getJSONArray("data");
                int length = jsonArraySKGrads.length();
                for (int i = 0; i < length; i++) {
                    sKGrade = new SKGrade();
                    jsonSKGrade = jsonArraySKGrads.getJSONObject(i);
                    sKGrade.setId(jsonSKGrade.optString("id"));
                    sKGrade.setName(jsonSKGrade.optString("name"));
                    sKClasses = new ArrayList<SkClasses>();
                    jsonArraySKClasses = jsonSKGrade.getJSONArray("list");
                    int length2 = jsonArraySKClasses.length();
                    for (int j = 0; j < length2; j++) {
                        jsonSkClass = jsonArraySKClasses.getJSONObject(j);
                        sKClass = new SkClasses();
                        sKClass.setId(jsonSkClass.optString("id"));
                        sKClass.setGradeId(jsonSkClass.optString("gradeId"));
                        sKClass.setName(jsonSkClass.optString("name"));
                        sKClass.setNum(jsonSkClass.optString("num"));
                        sKClasses.add(sKClass);
                    }
                    sKGrade.setClasses(sKClasses);
                    sKGrades.add(sKGrade);
                }
                return sKGrades;
            } catch (JSONException e) {
                return sKGrades;
            }
        } else {
            return sKGrades;
        }
    }

    // 地区解析
    public ArrayList<SKArea> resolveArea(String json) {
        ArrayList<SKArea> sKClasses = new ArrayList<SKArea>();
        SKArea sKArea = null;
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonSKAreas = jsonObject.getJSONArray("data");
                int length = jsonSKAreas.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonSKArea = jsonSKAreas.getJSONObject(i);
                    sKArea = new SKArea();
                    sKArea.setName(jsonSKArea.optString("name"));
                    sKArea.setAid(jsonSKArea.optString("aid"));
                    sKArea.setId(jsonSKArea.optString("id"));
                    sKClasses.add(sKArea);
                }
                return sKClasses;
            } catch (JSONException e) {
                return sKClasses;
            }
        }
        return sKClasses;
    }

    // 学科解析
    public ArrayList<SKArea> PaseSubject(String json) {
        ArrayList<SKArea> teacher_pase_list = new ArrayList<SKArea>();
        JSONObject jsonObject;
        SKArea teacher_Pase;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonSKAreas = jsonObject.getJSONArray("data");
            int length = jsonSKAreas.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonSKArea = jsonSKAreas.getJSONObject(i);
                teacher_Pase = new SKArea();
                teacher_Pase.setName(jsonSKArea.optString("name"));
                teacher_Pase.setId(jsonSKArea.optString("subjectId"));
                teacher_pase_list.add(teacher_Pase);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacher_pase_list;

    }

    // 阶段解析
    public ArrayList<SKArea> PaseJieDuan(String json) {
        ArrayList<SKArea> teacher_pase_list = new ArrayList<SKArea>();
        JSONObject jsonObject;
        SKArea teacher_Pase;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonSKAreas = jsonObject.getJSONArray("data");
            int length = jsonSKAreas.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonSKArea = jsonSKAreas.getJSONObject(i);
                teacher_Pase = new SKArea();
                teacher_Pase.setName(jsonSKArea.optString("name"));
                teacher_Pase.setId(jsonSKArea.optString("id"));
                teacher_pase_list.add(teacher_Pase);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacher_pase_list;
    }

    public ArrayList<SKTeacherOrSubject> resolveSubject(String json) {
        ArrayList<SKTeacherOrSubject> sKClasses = new ArrayList<SKTeacherOrSubject>();
        SKTeacherOrSubject sKSubject = null;
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonSKAreas = jsonObject.getJSONArray("data");
                int length = jsonSKAreas.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonSKArea = jsonSKAreas.getJSONObject(i);
                    sKSubject = new SKTeacherOrSubject();
                    sKSubject.setName(jsonSKArea.optString("name"));
                    sKSubject.setSubjectId(jsonSKArea.optString("subjectId"));
                    sKClasses.add(sKSubject);
                }
                return sKClasses;
            } catch (JSONException e) {
                return sKClasses;
            }
        }
        return sKClasses;
    }

    public ArrayList<SKMessageList> resolveMessage(String json) {
        ArrayList<SKMessageList> list = new ArrayList<SKMessageList>();
        SKMessageList sKMessageListdata = null;
        JSONObject jsonObject;
        ArrayList<SKMessage> sKMessageList;
        SKMessage skMessage;
        ArrayList<SkMessage_Res> SkMessage_ResList;
        SkMessage_Res skMessage_Res;
        ArrayList<SkMessage_Voice> skMessage_VoiceList;
        SkMessage_Voice skMessage_Voice;
        if (json != null) {
            try {
                jsonObject = new JSONObject(json);
                JSONArray optJSONArray = jsonObject.optJSONArray("data");
                int length = optJSONArray.length();
                for (int i = 0; i < length; i++) {// 这里每次循环代表一个日期
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    sKMessageListdata = new SKMessageList();
                    list.add(sKMessageListdata);
                    sKMessageListdata.setDate(optJSONObject.optString("date"));
                    sKMessageList = new ArrayList<SKMessage>();
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("list");
                    sKMessageListdata.setList(sKMessageList);
                    int length2 = optJSONArray2.length();
                    for (int j = 0; j < length2; j++) {// 这里每次循环代表日期下一条消息
                        JSONObject jsonObject2 = optJSONArray2.getJSONObject(j);
                        skMessage = new SKMessage();
                        sKMessageList.add(skMessage);
                        skMessage.setId(jsonObject2.optString("id"));
                        skMessage.setUid(jsonObject2.optString("uid"));
                        skMessage.setTeachUid(jsonObject2.optString("teachUid"));
                        skMessage.setSubjectId(jsonObject2.optString("subjectId"));
                        skMessage.setQuestionId(jsonObject2.optString("questionId"));
                        skMessage.setResNum(jsonObject2.optString("resNum"));
                        skMessage.setUpdateNum(jsonObject2.optString("updateNum"));
                        skMessage.setCreateTime(jsonObject2.optString("createTime"));
                        skMessage.setLast_uid(jsonObject2.optString("last_uid"));
                        skMessage.setAcknowledge(jsonObject2.optString("acknowledge"));
                        JSONObject optJSONObject2 = jsonObject2.optJSONObject("acknowledge_list");
                        if (optJSONObject2 != null) {
                            skAcknowledge_list = new SKAcknowledge_list();
                            skAcknowledge_list.setAnswer_messge(optJSONObject2.optString("answer_messge"));
                            skAcknowledge_list.setGift2points(optJSONObject2.optString("gift2points"));
                            skMessage.setsKAcknowledge_list(skAcknowledge_list);
                        }
                        skMessage.setComplain(jsonObject2.optString("complain"));
                        skMessage.setClaim_uid(jsonObject2.optString("claim_uid"));
                        skMessage.setClaim_time(jsonObject2.optString("claim_time"));
                        skMessage.setDone(jsonObject2.optBoolean("isDone"));
                        skMessage.setUpdateTime(jsonObject2.optString("updateTime"));
                        skMessage.setNewM(jsonObject2.optString("new"));
                        skMessage.setiSmyteach(jsonObject2.optString("iSmyteach"));
                        skMessage.setResCount(jsonObject2.optString("ResCount"));
                        skMessage.setDate(jsonObject2.optString("date"));
                        skMessage.setUserName(jsonObject2.optString("userName"));
                        skMessage.setIcon(jsonObject2.optString("icon"));
                        skMessage.setNickname(jsonObject2.optString("nickname"));
                        skMessage.setMsgType(jsonObject2.optString("msg_type"));
                        JSONArray optJSONArray3 = jsonObject2.optJSONArray("res");
                        SkMessage_ResList = new ArrayList<SkMessage_Res>();
                        skMessage.setRes(SkMessage_ResList);
                        if (optJSONArray3 == null) {
                            continue;
                        }
                        int length3 = optJSONArray3.length();
                        for (int k = 0; k < length3; k++) {
                            JSONObject jsonObject3 = optJSONArray3.getJSONObject(k);
                            skMessage_Res = new SkMessage_Res();
                            SkMessage_ResList.add(skMessage_Res);
                            skMessage_Res.setId(jsonObject3.optString("id"));
                            skMessage_Res.setUid(jsonObject3.optString("uid"));
                            skMessage_Res.setQuestionId(jsonObject3.optString("questionId"));
                            skMessage_Res.setFileId(jsonObject3.optString("fileId"));
                            skMessage_Res.setResId(jsonObject3.optString("resId"));
                            skMessage_Res.setTypes(jsonObject3.optString("types"));
                            skMessage_Res.setIsStudent(jsonObject3.optString("isStudent"));
                            skMessage_Res.setIsFirst(jsonObject3.optString("isFirst"));
                            skMessage_Res.setiSpublic(jsonObject3.optString("iSpublic"));
                            skMessage_Res.setFace(jsonObject3.optString("face"));
                            skMessage_Res.setFile(jsonObject3.optJSONObject("file").optString("url"));
                            skMessage_Res.setFile_tub(jsonObject3.optJSONObject("file").optString("tub"));
                            JSONArray optJSONArray4 = jsonObject3.optJSONArray("voice");
                            skMessage_VoiceList = new ArrayList<SkMessage_Voice>();
                            skMessage_Res.setVoice(skMessage_VoiceList);
                            if (optJSONArray4 == null) {
                                continue;
                            }
                            int length4 = optJSONArray4.length();
                            for (int l = 0; l < length4; l++) {
                                JSONObject jsonObject4 = optJSONArray4.getJSONObject(l);
                                skMessage_Voice = new SkMessage_Voice();
                                skMessage_VoiceList.add(skMessage_Voice);
                                skMessage_Voice.setId(jsonObject4.optString("id"));
                                skMessage_Voice.setUid(jsonObject4.optString("uid"));
                                skMessage_Voice.setQuestionId(jsonObject4.optString("questionId"));
                                skMessage_Voice.setFileId(jsonObject4.optString("fileId"));
                                skMessage_Voice.setResId(jsonObject4.optString("resId"));
                                skMessage_Voice.setTypes(jsonObject4.optString("types"));
                                skMessage_Voice.setIsStudent(jsonObject4.optString("isStudent"));
                                skMessage_Voice.setIsFirst(jsonObject4.optString("isFirst"));
                                skMessage_Voice.setiSpublic(jsonObject4.optString("iSpublic"));
                                skMessage_Voice.setFile(jsonObject4.optJSONObject("file").optString("url"));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        return list;
    }

    // 条件收索 我的老师
    public ArrayList<Star_Teacher_Parse> start_teather(String json) {
        ArrayList<Star_Teacher_Parse> list = new ArrayList<Star_Teacher_Parse>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                teacherParse = new Star_Teacher_Parse();
                JSONObject object2 = jsonArray.getJSONObject(i);
                teacherParse.setIcon(object2.getString("icon"));
                teacherParse.setNickname(object2.optString("nickname"));
                teacherParse.setSubjectid(object2.optString("subjectId"));
                teacherParse.setSubiect(object2.optString("subject"));
                teacherParse.setFormGrade(object2.optString("fromGrade"));
                teacherParse.setArea(object2.optString("area"));
                teacherParse.setGrade(object2.optString("grade"));
                teacherParse.setInfo(object2.optString("info"));
                teacherParse.setFansNum(object2.optString("fansNum"));
                teacherParse.setPoints(object2.optString("points"));
                teacherParse.setUid(object2.optString("uid"));
                teacherParse.setLike_num(object2.optString("like_num"));
                teacherParse.setClaim_question_num(object2.optString("claim_question_num"));
                teacherParse.isOnline = object2.optBoolean("isOnline");
                list.add(teacherParse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 老师信息
    public User_Info My_teather1(String json) {
        User_Info userinfo = null;
        try {
            JSONObject object = new JSONObject(json);
            userinfo = new User_Info();
            JSONObject objectitme = object.getJSONObject("data");
            String points = objectitme.optString("points");
            String icon = objectitme.optString("icon");
            String questions = objectitme.optString("questions");
            userinfo.setFansNum(objectitme.optString("fansNum"));
            userinfo.setGreatid(objectitme.optString("subject"));
            userinfo.setSubjectId(objectitme.optString("subjectId"));
            userinfo.setFromGradeId(objectitme.optString("fromGradeId"));
            userinfo.setToGradeId(objectitme.optString("toGradeId"));
            userinfo.setShool(objectitme.optString("school"));
            userinfo.setEmail(objectitme.optString("email"));
            userinfo.setMob(objectitme.optString("mob"));
            userinfo.setInfo(objectitme.optString("info"));
            userinfo.setNickname(objectitme.optString("nickname"));
            userinfo.setGrade(objectitme.optString("grade"));
            userinfo.setBirthday(objectitme.optString("birthday"));
            userinfo.setSex(objectitme.optString("sex"));
            userinfo.setName(objectitme.optString("name"));
            userinfo.setArea(objectitme.optString("aId"));
            userinfo.setGradeName(objectitme.optString("gradeName"));
            userinfo.mTeachers = objectitme.optInt("teachers");
            userinfo.isonline = objectitme.optBoolean("isOnline");
            userinfo.is_myteacher = objectitme.optBoolean("is_myteacher");

            userinfo.setLike_num(objectitme.optString("likes"));
            userinfo.setPoints(points);
            userinfo.setQuestions(questions);


            JSONObject jsonObject = objectitme.optJSONObject("url");
            if (jsonObject != null) {// 这地方,获取图片地址应该放在下面的代码之前.不然下面出异常了,这里的代码都走不到.//徐斌.2014.8.7
                userinfo.setPicurl(jsonObject.optString("tub"));
            }
            JSONObject objec_tea_info = objectitme.optJSONObject("teacher_info");
            if (objec_tea_info != null) {
                LogUtil.i("读取下面的老师信息");
                userinfo.setClaim_question_num(objec_tea_info.optString("claim_question_num"));
                userinfo.setLike_num(objec_tea_info.optString("like_num"));
                userinfo.setSubject(objec_tea_info.optString("subject"));
                userinfo.setSubjectid(objec_tea_info.optString("subjectId"));
                userinfo.setUid(objec_tea_info.optString("uid"));
                userinfo.setPoints(points);
                userinfo.setIcon(icon);
                userinfo.setQuestions(questions);

                userinfo.bankId = objec_tea_info.optString("bankId");// 兑换现金时的银行id
                userinfo.bankName = objec_tea_info.optString("bankName");//
                userinfo.bankAddr = objec_tea_info.optString("bankAddr");// 开户行名称
                userinfo.bankNO = objec_tea_info.optString("bankNO");// 银行卡号
                userinfo.receiver = objec_tea_info.optString("receiver");// 兑换商品时收件人名字
                userinfo.phone = objec_tea_info.optString("phone");// 兑换商品手机号码
                userinfo.addressee = objec_tea_info.optString("addressee");
                userinfo.paper = objec_tea_info.optString("Paper");
            } else {
                LogUtil.i("这是学生账户,停止读取下面的老师信息");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userinfo;
    }

    // 我的时刻解析
    public User_Info My_teather(String json) {
        User_Info userinfo = null;
        try {
            JSONObject object = new JSONObject(json);
            userinfo = new User_Info();
            JSONObject objectitme = object.getJSONObject("data");
            String questions = objectitme.optString("questions");
            // String gradename = objectitme.optString("gradeName");
            userinfo.setFansNum(objectitme.optString("fansNum"));
            userinfo.setSubject(objectitme.optString("subject"));
            userinfo.setGreatid(objectitme.optString("gradeId"));
            userinfo.setFromGradeId(objectitme.optString("fromGradeId"));
            userinfo.setToGradeId(objectitme.optString("toGradeId"));
            userinfo.setClaim_question_num(objectitme.optString("claim_question_num"));
            userinfo.setLike_num(objectitme.optString("like_num"));
            userinfo.setInfo(objectitme.optString("info"));
            userinfo.setSubjectid(objectitme.optString("subjectId"));
            userinfo.setName(objectitme.optString("name"));
            userinfo.setNickname(objectitme.optString("nickname"));
            userinfo.setPoints(objectitme.optString("points"));
            userinfo.setGrade(objectitme.optString("grade"));
            userinfo.setGradeName(objectitme.optString("gradeName"));
            JSONObject jsonObject2 = objectitme.optJSONObject("url");
            if (jsonObject2 != null) {
                userinfo.setPicurl(jsonObject2.optString("tub"));
            }
            userinfo.setQuestions(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userinfo;
    }

    // 解析新消�?
    public ArrayList<SKMessage> resolveNewMessage(String json) {
        ArrayList<SKMessage> sKMessageList = new ArrayList<SKMessage>();
        JSONObject jsonObject;
        JSONObject jsonObject1;
        SKMessage skMessage;
        ArrayList<SkMessage_Res> SkMessage_ResList;
        SkMessage_Res skMessage_Res;
        ArrayList<SkMessage_Voice> skMessage_VoiceList;
        SkMessage_Voice skMessage_Voice;
        if (json != null) {
            try {
                jsonObject = new JSONObject(json);
                jsonObject1 = jsonObject.optJSONObject("data");
                JSONArray optJSONArray2 = jsonObject1.optJSONArray("list");
                int length2 = optJSONArray2.length();
                for (int j = 0; j < length2; j++) {
                    JSONObject jsonObject2 = optJSONArray2.getJSONObject(j);
                    skMessage = new SKMessage();
                    sKMessageList.add(skMessage);
                    skMessage.setId(jsonObject2.optString("id"));
                    skMessage.setUid(jsonObject2.optString("uid"));
                    skMessage.setTeachUid(jsonObject2.optString("teachUid"));
                    skMessage.setSubjectId(jsonObject2.optString("subjectId"));
                    skMessage.setQuestionId(jsonObject2.optString("questionId"));
                    skMessage.setResNum(jsonObject2.optString("resNum"));
                    skMessage.setUpdateNum(jsonObject2.optString("updateNum"));
                    skMessage.setCreateTime(jsonObject2.optString("createTime"));
                    skMessage.setLast_uid(jsonObject2.optString("last_uid"));
                    skMessage.setAcknowledge(jsonObject2.optString("acknowledge"));
                    JSONObject optJSONObject2 = jsonObject2.optJSONObject("acknowledge_list");
                    if (optJSONObject2 != null) {
                        skAcknowledge_list = new SKAcknowledge_list();
                        skAcknowledge_list.setAnswer_messge(optJSONObject2.optString("answer_messge"));
                        skAcknowledge_list.setGift2points(optJSONObject2.optString("gift2points"));
                        skMessage.setsKAcknowledge_list(skAcknowledge_list);
                    }
                    skMessage.setComplain(jsonObject2.optString("complain"));
                    skMessage.setClaim_uid(jsonObject2.optString("claim_uid"));
                    skMessage.setClaim_time(jsonObject2.optString("claim_time"));
                    skMessage.setDone(jsonObject2.optBoolean("isDone"));
                    skMessage.setUpdateTime(jsonObject2.optString("updateTime"));
                    skMessage.setNewM(jsonObject2.optString("new"));
                    skMessage.setiSmyteach(jsonObject2.optString("iSmyteach"));
                    skMessage.setResCount(jsonObject2.optString("ResCount"));
                    skMessage.setDate(jsonObject2.optString("date"));
                    skMessage.setUserName(jsonObject2.optString("userName"));
                    skMessage.setIcon(jsonObject2.optString("icon"));
                    skMessage.setNickname(jsonObject2.optString("nickname"));

                    JSONArray optJSONArray3 = jsonObject2.optJSONArray("res");

                    SkMessage_ResList = new ArrayList<SkMessage_Res>();
                    skMessage.setRes(SkMessage_ResList);
                    if (optJSONArray3 == null) {
                        continue;
                    }
                    int length3 = optJSONArray3.length();
                    for (int k = 0; k < length3; k++) {
                        JSONObject jsonObject3 = optJSONArray3.getJSONObject(k);
                        skMessage_Res = new SkMessage_Res();
                        SkMessage_ResList.add(skMessage_Res);
                        skMessage_Res.setId(jsonObject3.optString("id"));
                        skMessage_Res.setUid(jsonObject3.optString("uid"));
                        skMessage_Res.setQuestionId(jsonObject3.optString("questionId"));
                        skMessage_Res.setFileId(jsonObject3.optString("fileId"));
                        skMessage_Res.setResId(jsonObject3.optString("resId"));
                        skMessage_Res.setTypes(jsonObject3.optString("types"));
                        skMessage_Res.setIsStudent(jsonObject3.optString("isStudent"));
                        skMessage_Res.setIsFirst(jsonObject3.optString("isFirst"));
                        skMessage_Res.setiSpublic(jsonObject3.optString("iSpublic"));
                        skMessage_Res.setFace(jsonObject3.optString("face"));
                        skMessage_Res.setFile(jsonObject3.optJSONObject("file").optString("url"));
                        skMessage_Res.setFile_tub(jsonObject3.optJSONObject("file").optString("tub"));
                        JSONArray optJSONArray4 = jsonObject3.optJSONArray("voice");
                        skMessage_VoiceList = new ArrayList<SkMessage_Voice>();
                        skMessage_Res.setVoice(skMessage_VoiceList);
                        if (optJSONArray4 == null) {
                            continue;
                        }
                        int length4 = optJSONArray4.length();
                        for (int l = 0; l < length4; l++) {

                            JSONObject jsonObject4 = optJSONArray4.getJSONObject(l);
                            skMessage_Voice = new SkMessage_Voice();
                            skMessage_VoiceList.add(skMessage_Voice);
                            skMessage_Voice.setId(jsonObject4.optString("id"));
                            skMessage_Voice.setUid(jsonObject4.optString("uid"));
                            skMessage_Voice.setQuestionId(jsonObject4.optString("questionId"));
                            skMessage_Voice.setFileId(jsonObject4.optString("fileId"));
                            skMessage_Voice.setResId(jsonObject4.optString("resId"));
                            skMessage_Voice.setTypes(jsonObject4.optString("types"));
                            skMessage_Voice.setIsStudent(jsonObject4.optString("isStudent"));
                            skMessage_Voice.setIsFirst(jsonObject4.optString("isFirst"));
                            skMessage_Voice.setiSpublic(jsonObject4.optString("iSpublic"));
                            skMessage_Voice.setFile(jsonObject4.optJSONObject("file").optString("url"));
                        }
                    }
                }
            } catch (JSONException e) {
                return sKMessageList;
            }
        }
        return sKMessageList;
    }

    public ArrayList<SKMessage> resolveISDoneNewMessage(String json) {
        ArrayList<SKMessage> sKMessageList = new ArrayList<SKMessage>();
        JSONObject jsonObject;
        JSONObject jsonObject1;
        SKMessage skMessage;
        if (json != null) {
            try {
                jsonObject = new JSONObject(json);
                jsonObject1 = jsonObject.optJSONObject("data");
                JSONArray optJSONArray2 = jsonObject1.optJSONArray("isDoneList");
                int length2 = optJSONArray2.length();
                for (int j = 0; j < length2; j++) {
                    skMessage = new SKMessage();
                    sKMessageList.add(skMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sKMessageList;
    }

    // 表情�?获取
    public Fase_1 Face_Package_Pase(String json) {
        Fase_1 fase_1 = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray datalist = jsonObject.getJSONArray("data");
            for (int i = 0; i < datalist.length(); i++) {
                JSONObject data = datalist.getJSONObject(i);
                fase_1 = new Fase_1();
                fase_1.setId(data.optString("id"));
                fase_1.setFileId(data.optString("fileId"));
                fase_1.setName(data.optString("name"));
                fase_1.setPrice(data.optString("price"));
                fase_1.setCount(data.optJSONObject("face_img").optString("url"));
                ArrayList<Fase_Packs> Fase_Packs_List = new ArrayList<Fase_Packs>();
                fase_1.setRes(Fase_Packs_List);
                JSONArray listPacks = data.getJSONArray("list");
                for (int j = 0; j < listPacks.length(); j++) {
                    Fase_Packs fase_Packs = new Fase_Packs();
                    JSONObject object = listPacks.getJSONObject(j);
                    fase_Packs.setId(object.optString("id"));
                    fase_Packs.setFileId(object.optString("fileId"));
                    fase_Packs.setFase_url(object.getJSONObject("face").optString("url"));
                    Fase_Packs_List.add(fase_Packs);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fase_1;
    }

    // 获取GIF 图片
    public ArrayList<Send_Gife> Huo_Gif(String json) {
        ArrayList<Send_Gife> Fase_Packs_List = new ArrayList<Send_Gife>();
        JSONObject jsonObject;
        Send_Gife gif_picture = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray datalist = jsonObject.getJSONArray("data");
            for (int i = 0; i < datalist.length(); i++) {
                gif_picture = new Send_Gife();
                JSONObject object = datalist.getJSONObject(i);
                gif_picture.setId(object.optString("id"));
                gif_picture.setPoints(object.optString("points"));
                gif_picture.setName(object.optString("name"));
                gif_picture.setFileId(object.optString("fileId"));
                gif_picture.setFaceuri(object.getJSONObject("face").optString("url"));
                Fase_Packs_List.add(gif_picture);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Fase_Packs_List;
    }

    // 获取题库的数�?
    public ArrayList<Question_Bank> Count_Pase(String json) {
        ArrayList<Question_Bank> arrayList = new ArrayList<Question_Bank>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("data");
            for (int i = 0; i < list.length(); i++) {
                JSONObject array = list.getJSONObject(i);
                Question_Bank bank = new Question_Bank();
                arrayList.add(bank);
                bank.setQuestion(array.optString("questionId"));
                bank.setName(array.optString("name"));
                bank.setData(array.optString("date"));
                bank.setSubject(array.optString("subject"));
                bank.setCid(array.optString("cid"));
                // bank.setCategory(array.optBoolean("category"));
                bank.setCategory1(array.optString("category"));
                JSONArray optJSONArray3 = array.optJSONArray("res");
                ArrayList<SkMessage_Res> SkMessage_ResList = new ArrayList<SkMessage_Res>();
                bank.setRes(SkMessage_ResList);
                if (optJSONArray3 == null) {
                    continue;
                }
                int length3 = optJSONArray3.length();
                for (int k = 0; k < length3; k++) {
                    JSONObject jsonObject3 = optJSONArray3.getJSONObject(k);
                    SkMessage_Res skMessage_Res = new SkMessage_Res();
                    SkMessage_ResList.add(skMessage_Res);
                    skMessage_Res.setId(jsonObject3.optString("id"));
                    skMessage_Res.setUid(jsonObject3.optString("uid"));
                    skMessage_Res.setQuestionId(jsonObject3.optString("questionId"));
                    skMessage_Res.setFileId(jsonObject3.optString("fileId"));
                    skMessage_Res.setResId(jsonObject3.optString("resId"));
                    skMessage_Res.setTypes(jsonObject3.optString("types"));
                    skMessage_Res.setIsStudent(jsonObject3.optString("isStudent"));
                    skMessage_Res.setIsFirst(jsonObject3.optString("isFirst"));
                    skMessage_Res.setiSpublic(jsonObject3.optString("iSpublic"));
                    skMessage_Res.setFace(jsonObject3.optString("face"));
                    String url = jsonObject3.optJSONObject("file").optString("url");
                    skMessage_Res.setFile_tub(jsonObject3.optJSONObject("file").optString("tub"));
                    if (url != null) {
                        skMessage_Res.setFile(url);
                    }
                    JSONArray optJSONArray4 = jsonObject3.optJSONArray("voice");
                    ArrayList<SkMessage_Voice> skMessage_VoiceList = new ArrayList<SkMessage_Voice>();
                    skMessage_Res.setVoice(skMessage_VoiceList);
                    if (optJSONArray4 == null) {
                        continue;
                    }
                    int length4 = optJSONArray4.length();
                    for (int l = 0; l < length4; l++) {
                        JSONObject jsonObject4 = optJSONArray4.getJSONObject(l);
                        SkMessage_Voice skMessage_Voice = new SkMessage_Voice();
                        skMessage_VoiceList.add(skMessage_Voice);
                        skMessage_Voice.setId(jsonObject4.optString("id"));
                        skMessage_Voice.setUid(jsonObject4.optString("uid"));
                        skMessage_Voice.setQuestionId(jsonObject4.optString("questionId"));
                        skMessage_Voice.setFileId(jsonObject4.optString("fileId"));
                        skMessage_Voice.setResId(jsonObject4.optString("resId"));
                        skMessage_Voice.setTypes(jsonObject4.optString("types"));
                        skMessage_Voice.setIsStudent(jsonObject4.optString("isStudent"));
                        skMessage_Voice.setIsFirst(jsonObject4.optString("isFirst"));
                        skMessage_Voice.setiSpublic(jsonObject4.optString("iSpublic"));
                        skMessage_Voice.setFile(jsonObject4.optJSONObject("file").optString("url"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    // 綜合數據
    public com.yshow.shike.entity.Soft_Info Soft_Info(String json) {
        com.yshow.shike.entity.Soft_Info info = new Soft_Info();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");

            JSONObject version = data.getJSONObject("version");
            JSONObject android = version.getJSONObject("android");
            info.newversion = android.getString("NO");
            info.updateurl = android.getString("url");

            JSONObject agreement = data.getJSONObject("agreement");
            info.agreement = agreement.getString("text");
            info.agreementurl = agreement.getString("url");

            JSONObject introduce = data.getJSONObject("introduce");
            info.introduce = introduce.getString("text");
            info.introduceurl = introduce.getString("url");

            JSONObject FAQ = data.getJSONObject("FAQ");
            info.FAQ = FAQ.getString("text");
            info.FAQurl = FAQ.getString("url");

            JSONObject FAQ_T = data.getJSONObject("FAQ_T");
            info.FAQ_T = FAQ_T.getString("text");
            info.FAQ_Turl = FAQ_T.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    // 条件收索
    public ArrayList<Star_Teacher_Parse> Search_Terms(String json) {
        ArrayList<Star_Teacher_Parse> list = new ArrayList<Star_Teacher_Parse>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object2 = jsonArray.getJSONObject(i);
                JSONArray array = object2.getJSONArray("list");
                for (int j = 0; j < array.length(); j++) {
                    teacherParse = new Star_Teacher_Parse();
                    JSONObject object3 = array.getJSONObject(j);
                    teacherParse.setIcon(object3.getString("icon"));
                    teacherParse.setNickname(object3.optString("nickname"));
                    teacherParse.setSubjectid(object3.optString("subjectId"));
                    teacherParse.setSubiect(object3.optString("subject"));
                    teacherParse.setFormGrade(object3.optString("fromGrade"));
                    teacherParse.setGrade(object3.optString("grade"));
                    teacherParse.setFormGrade_name(object3.optString("fromGrade_num"));
                    teacherParse.setToGrade_name(object3.optString("toGrade_num"));
                    teacherParse.setArea(object3.optString("area"));
                    teacherParse.setInfo(object3.optString("info"));
                    teacherParse.setFansNum(object3.optString("fansNum"));
                    teacherParse.setPoints(object3.optString("points"));
                    teacherParse.setUid(object3.optString("uid"));
                    teacherParse.setClaim_question_num(object3.optString("claim_question_num"));
                    teacherParse.setLike_num(object3.optString("like_num"));
                    teacherParse.setiSmyteath(object3.optString("iSmyteach"));
                    teacherParse.isOnline = object3.optBoolean("isOnline");
                    list.add(teacherParse);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 用于提问时解析指定科目下面有没有对应老师
     */
    public ArrayList<SKTeacherOrSubject> resolveTeacher(String json, String subjectId) {
        ArrayList<SKTeacherOrSubject> sKClasses = new ArrayList<SKTeacherOrSubject>();
        SKTeacherOrSubject steacher = null;
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonObject = jsonObject.getJSONObject("data");
                JSONArray names = jsonObject.names();
                int length = names.length();
                for (int i = 0; i < length; i++) {
                    JSONArray jsonArray = jsonObject.getJSONArray(names.getString(i));
                    int length2 = jsonArray.length();
                    for (int j = 0; j < length2; j++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                        if (jsonObject2.optString("subjectId").equals(subjectId)) {
                            steacher = new SKTeacherOrSubject();
                            steacher.setName(jsonObject2.optString("nickname"));
                            steacher.setSubjectId(jsonObject2.optString("subjectId"));
                            steacher.setTeacherId(jsonObject2.optString("uid"));
                            steacher.subject = jsonObject2.optString("subject");
                            steacher.isOnline = jsonObject2.optBoolean("isOnline");
                            JSONObject imgurl = jsonObject2.optJSONObject("url");
                            if (imgurl != null) {
                                steacher.icon = imgurl.optString("tub");
                            }
                            sKClasses.add(steacher);
                        }

                    }
                }
                return sKClasses;
            } catch (JSONException e) {
                return sKClasses;
            }
        }
        return sKClasses;
    }

    // 我的老师
    public ArrayList<Star_Teacher_Parse> My_Teather(String json) {
        ArrayList<Star_Teacher_Parse> myTeacherList = new ArrayList<Star_Teacher_Parse>();
        JSONObject object;
        try {
            object = new JSONObject(json);
            dataobject = object.getJSONObject("data");
            JSONArray namesarray = dataobject.names();
            for (int i = 0; i < namesarray.length(); i++) {
                JSONArray array = dataobject.getJSONArray(namesarray.getString(i));
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonObject = array.getJSONObject(j);
                    Star_Teacher_Parse parse = new Star_Teacher_Parse();
                    parse.setSubiect(jsonObject.optString("subject"));
                    parse.setNickname(jsonObject.optString("nickname"));
                    parse.setInfo(jsonObject.optString("info"));
                    parse.setFansNum(jsonObject.optString("fansNum"));
                    parse.setPoints(jsonObject.optString("points"));
                    parse.setiSmyteath(jsonObject.optInt("iSmyteach") + "");
                    parse.setUid(jsonObject.optString("uid"));
                    parse.setLike_num(jsonObject.optString("like_num"));
                    parse.setSubjectid(jsonObject.optString("subjectId"));
                    parse.setClaim_question_num(jsonObject.optString("claim_question_num"));

                    parse.isOnline = jsonObject.optBoolean("isOnline");
                    JSONObject optJSONObject = jsonObject.optJSONObject("url");
                    if (optJSONObject != null) {
                        String optString = optJSONObject.optString("url");
                        parse.setIcon(optString);
                    }
                    parse.setName(jsonObject.optString("name"));
                    myTeacherList.add(parse);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myTeacherList;
    }

    public Student_Info Student_Info_Pase(String json) {
        Student_Info student_Info = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONObject jsonObject = object.getJSONObject("data");
            student_Info = new Student_Info();
            student_Info.setNickname(jsonObject.optString("nickname"));
            student_Info.areaid = jsonObject.optString("aId");
            student_Info.setName(jsonObject.optString("name"));
            student_Info.setGrade(jsonObject.optString("grade"));
            student_Info.setGradeName(jsonObject.optString("gradeName"));
            student_Info.setInfo(jsonObject.optString("info"));
            student_Info.setQuestions(jsonObject.optString("questions"));
            student_Info.area = jsonObject.optString("area_name");
            JSONObject urlobject = jsonObject.getJSONObject("url");
            if (urlobject != null) {
                student_Info.setIcon(urlobject.optString("tub"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return student_Info;
    }

    // 获取文件分类接口
    public List<File_Paser> Paser_File(String json) {
        List<File_Paser> list_File = new ArrayList<File_Paser>();
        File_Paser file_Paser = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("data");
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = list.getJSONObject(i);
                file_Paser = new File_Paser();
                file_Paser.setId(object.optString("id"));
                file_Paser.setUid(object.optString("uid"));
                file_Paser.setName(object.optString("name"));
                file_Paser.setCid(object.optString("cid"));
                list_File.add(file_Paser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_File;
    }

    public File_Paser Paser_File2(String json) {
        File_Paser file_Paser = new File_Paser();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject list = jsonObject.getJSONObject("data");
            file_Paser.setUid(list.optString("uid"));
            file_Paser.setName(list.optString("name"));
            file_Paser.setCid(list.optString("cid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return file_Paser;
    }

    // 解析創建�?
    public Creat_File Pase_Creat_File(String json) {
        JSONObject jsonObject;
        Creat_File file = null;
        try {
            jsonObject = new JSONObject(json);
            JSONObject object = jsonObject.getJSONObject("data");
            file = new Creat_File();
            file.setName(object.optString("uid"));
            file.setName(object.optString("name"));
            file.setUid(object.optString("cid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return file;
    }

    // 解析数据
    public Object getAllData(String json, Object obj) {
        Type tp = new TypeToken<Object>() {
        }.getType();
        Gson gs = new Gson();
        Object o = gs.fromJson(json, tp);
        return o;
    }

    public TeacherXianjin getPointFromGSON(String json) {
        Type type = new TypeToken<TeacherXianjin>() {
        }.getType();
        Gson gson = new Gson();
        TeacherXianjin tx = gson.fromJson(json, type);
        return tx;
    }

    // 按电话条�?搜索
    public Search_Mob Mob_Sear(String json) {
        Search_Mob search_Mob = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            JSONObject object = jsonObject.getJSONObject("data");
            search_Mob = new Search_Mob();
            search_Mob.setSubject(object.optString("subject"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return search_Mob;
    }

    // 我的老师
    public ArrayList<ArrayList<Star_Teacher_Parse>> My_Tea_Pase(String json) {
        ArrayList<ArrayList<Star_Teacher_Parse>> list = new ArrayList<ArrayList<Star_Teacher_Parse>>();
        // My_Student_Info my_Student_Info = null;
        try {
            // my_Student_Info = new My_Student_Info();
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            ArrayList<Star_Teacher_Parse> info_list = new ArrayList<Star_Teacher_Parse>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // my_Student_Info.setQuestions(jsonObject.optString("questions"));
                Star_Teacher_Parse stu_info = new Star_Teacher_Parse();
                JSONObject obj_stu_info = jsonObject.getJSONObject("info");

                JSONObject imgObj = jsonObject.optJSONObject("url");
                if (imgObj != null) {
                    stu_info.setIcon(imgObj.optString("tub"));
                }

                stu_info.setName(obj_stu_info.optString("name"));
                stu_info.setNickname(obj_stu_info.optString("nickname"));
                stu_info.setGrade(obj_stu_info.optString("grade"));
                stu_info.setGradeName(obj_stu_info.optString("gradeName"));
                stu_info.setInfo(obj_stu_info.optString("info"));
                stu_info.setPoints(obj_stu_info.optString("points"));
                stu_info.setQuestions(obj_stu_info.optString("questions"));
                info_list.add(stu_info);
            }
            list.add(info_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 我的学生
    public ArrayList<Star_Teacher_Parse> parseMyStudent(String json) {
        ArrayList<Star_Teacher_Parse> list = new ArrayList<Star_Teacher_Parse>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Star_Teacher_Parse stu_info = new Star_Teacher_Parse();
                JSONObject obj_stu_info = jsonObject.getJSONObject("info");

                JSONObject imgObj = obj_stu_info.optJSONObject("url");
                if (imgObj != null) {
                    stu_info.setIcon(imgObj.optString("tub"));
                }

                stu_info.setName(obj_stu_info.optString("name"));
                stu_info.setNickname(obj_stu_info.optString("nickname"));
                stu_info.setGrade(obj_stu_info.optString("grade"));
                stu_info.setGradeName(obj_stu_info.optString("gradeName"));
                stu_info.setInfo(obj_stu_info.optString("info"));
                stu_info.setPoints(obj_stu_info.optString("points"));
                stu_info.setQuestions(obj_stu_info.optString("questions"));
                stu_info.areaName = obj_stu_info.optString("area_name");
                list.add(stu_info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 账户信息数据查询
    public List<Count_Info> Count_Info_pase(String json) {
        List<Count_Info> list_info = new ArrayList<Count_Info>();
        Count_Info info = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                info = new Count_Info();
                info.setId(item.optString("id"));
                info.setUid(item.optString("uid"));
                info.setQuestionId(item.optString("questionId"));
                info.setPoints(item.optString("points"));
                info.setTypes(item.optString("types"));
                info.setMessge(item.optString("messge"));
                info.setAdds(item.optString("adds"));
                info.setNum(item.optString("num"));
                info.setDates(item.optString("Dates"));
                info.setH(item.optString("H"));
                info.setI(item.optString("i"));
                list_info.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_info;
    }

    public VersionModel getNewVersion(String json) {
        VersionModel versionmodel = new VersionModel();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            JSONObject version = data.getJSONObject("version");
            JSONObject android = version.getJSONObject("android");
            versionmodel.version = android.getString("NO");
            versionmodel.url = android.getString("url");
            versionmodel.isForce = android.optBoolean("isForce");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return versionmodel;
    }

    // 字节提问 走的接口
    public Auto_Login AuTo_Pase(String json) {
        Auto_Login auto_Login = null;
        try {
            auto_Login = new Auto_Login();
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            auto_Login.setUid(data.optString("uid"));
            auto_Login.setNickname(data.optString("nickname"));
            auto_Login.mob = data.optString("mob");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auto_Login;
    }

    // 解析问题id
    public Ceater_Question Pase_Questionid(String json) {
        Ceater_Question question = null;
        try {
            question = new Ceater_Question();
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            question.setQuestionid(data.optString("questionId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return question;
    }

    /**
     * 解析银联订单信息
     *
     * @param json
     * @return
     */
    public YinLianOrderModel GetYinlianOrder(String json) {
        YinLianOrderModel model = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            Gson gson = new Gson();
            model = gson.fromJson(data.toString(), YinLianOrderModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 解析首页广告信息
     *
     * @param json
     * @return
     */
    public ArrayList<HomeImgModel> getHomePageImgs(String json) {
        ArrayList<HomeImgModel> list = new ArrayList<HomeImgModel>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            int size = data.length();
            for (int i = 0; i < size; i++) {
                HomeImgModel model = new HomeImgModel();
                JSONObject obj = data.getJSONObject(i);
                model.id = obj.optString("id");
                model.pic = obj.optString("pic");
                model.title = obj.optString("title");
                model.url = obj.optString("url");
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}