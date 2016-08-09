package com.zuoyupeng.zaker.json;

import android.text.TextUtils;
import android.util.Log;

import com.zuoyupeng.zaker.bean.BeijingBean;
import com.zuoyupeng.zaker.bean.ChannelTab;
import com.zuoyupeng.zaker.bean.Commbean;
import com.zuoyupeng.zaker.bean.CommunityBean;
import com.zuoyupeng.zaker.bean.HotBean;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.bean.PlayBean;
import com.zuoyupeng.zaker.bean.ReadBean;
import com.zuoyupeng.zaker.bean.SpBean;
import com.zuoyupeng.zaker.bean.SpecialBean;
import com.zuoyupeng.zaker.fragment.ReadFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetJson {

    //获取搜索列表每一条内的内容数据
    public static List<NewsTab> searchNews(String json) {
        List<NewsTab> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                NewsTab newsTab = new NewsTab();
                newsTab.titleCha = jsonObject2.getString("title");
                newsTab.list_icon = jsonObject2.getString("list_icon");

                newsTab.list = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject2.getJSONArray("sons");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                    NewsTab.SubTab s = newsTab.new SubTab();
                    s.titleNews = jsonObject3.optString("title");//标题
                    s.pic = jsonObject3.optString("pic");//图片
                    s.block_color = jsonObject3.optString("block_color");//背景颜色
                    s.api_url = jsonObject3.optString("api_url");//新闻内容地址
                    if (!TextUtils.isEmpty(s.pic))
                        newsTab.list.add(s);
                }
                list.add(newsTab);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //新闻内容解析
    public static List<ReadBean> ReadNews(String json) {
        List<ReadBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            ReadBean readBean = new ReadBean();

            JSONObject job = jsonObject1.optJSONObject("info");
            readBean.next_url = job.optString("next_url");
            //第一
            if(!jsonObject1.isNull("block_info")) {
                JSONObject jsonObject7 = jsonObject1.optJSONObject("block_info");
                readBean.block_title = jsonObject7.optString("block_title");
            }
            //第二
            readBean.articles = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.optJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                ReadBean.ArticlesItem articlesItem = readBean.new ArticlesItem();
                articlesItem.auther_name = jsonObject2.optString("auther_name");//来源地（新华社）
                articlesItem.date = jsonObject2.optString("date");//时间
                articlesItem.title = jsonObject2.optString("title");//新闻标题
                articlesItem.weburl = jsonObject2.optString("weburl");//显示在第一位

                articlesItem.media = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject2.optJSONArray("media");
                if (jsonArray1 != null) {
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject3 = jsonArray1.optJSONObject(j);
                        ReadBean.MediaItem mediaItem = readBean.new MediaItem();
                        mediaItem.url = jsonObject3.optString("url");//大图网址
                        articlesItem.media.add(mediaItem);
                    }
                }
                readBean.articles.add(articlesItem);
            }

            JSONObject jsonObject4 = jsonObject1.optJSONObject("ipadconfig");//第三
            readBean.pages = new ArrayList<>();
            JSONArray jsonArray2 = jsonObject4.optJSONArray("pages");
            for (int x = 0; x < jsonArray2.length(); x++) {
                JSONObject jsonObject5 = jsonArray2.optJSONObject(x);
                ReadBean.PagesItem pagesItem = readBean.new PagesItem();
                JSONObject jsonObject6 = jsonObject5.optJSONObject("diy");
                pagesItem.bgimage_url = jsonObject6.optString("bgimage_url");
                readBean.pages.add(pagesItem);
            }
            list.add(readBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //社区内容解析
    public static List<CommunityBean> communityJson(String json) {
        List<CommunityBean> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            JSONArray jsonArray = jsonObject1.optJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                CommunityBean communityBean = new CommunityBean();
                communityBean.api_url = jsonObject2.optString("api_url");
                communityBean.pic = jsonObject2.optString("pic");
                communityBean.stitle = jsonObject2.optString("stitle");
                communityBean.title = jsonObject2.optString("title");
                list.add(communityBean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<HotBean> hotJSon(String json) {
        List<HotBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            JSONArray jsonArray = jsonObject1.optJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                HotBean hotBean = new HotBean();
                hotBean.auther_name = jsonObject2.optString("auther_name");
                hotBean.title = jsonObject2.optString("title");
                hotBean.weburl = jsonObject2.optString("weburl");
                hotBean.date = jsonObject2.optString("date");
                JSONObject object = jsonObject2.optJSONObject("special_info");
                if (object.isNull("item_type")) {
                    hotBean.item_type = null;
                } else {
                    hotBean.item_type = object.optString("item_type");
                }

                JSONArray jsonArray1 = jsonObject2.optJSONArray("thumbnail_medias");
                hotBean.thumbnail_medias = new ArrayList<>();
                if (jsonArray1 != null) {
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject3 = jsonArray1.optJSONObject(j);
                        HotBean.Medias medias = hotBean.new Medias();
                        medias.url = jsonObject3.optString("url");
                        hotBean.thumbnail_medias.add(medias);
                    }
                }
                list.add(hotBean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<PlayBean> playJson(String json) {
        List<PlayBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            PlayBean playBean = new PlayBean();

            JSONObject jsonObject2 = jsonObject1.optJSONObject("info");
            playBean.next_url = jsonObject2.optString("next_url");

            playBean.columns = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.optJSONArray("columns");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = jsonArray.optJSONObject(i);
                PlayBean.ColumnsItem columnsItem = playBean.new ColumnsItem();

                columnsItem.items = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject3.optJSONArray("items");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject4 = jsonArray1.optJSONObject(j);
                    PlayBean.Items items = playBean.new Items();
                    items.content = jsonObject4.optString("content");
                    items.title = jsonObject4.optString("title");

                    JSONObject jsonObject5 = jsonObject4.optJSONObject("article");
                    items.weburl = jsonObject5.optString("weburl");
                    JSONObject jsonObject6 = jsonObject4.optJSONObject("pic");
                    items.url = jsonObject6.optString("url");
                    columnsItem.items.add(items);
                }
                playBean.columns.add(columnsItem);
            }

            playBean.promote = new ArrayList<>();
            JSONArray Array = jsonObject1.optJSONArray("promote");
            for (int x = 0; x < Array.length(); x++) {
                JSONObject object = Array.optJSONObject(x);
                PlayBean.PromoteItem promoteItem = playBean.new PromoteItem();
                promoteItem.promotion_img = object.optString("promotion_img");
                if (!object.isNull("web")) {
                    JSONObject object2 = object.optJSONObject("web");
                    promoteItem.url = object2.optString("url");
                }
                if (!object.isNull("article")) {
                    JSONObject object1 = object.optJSONObject("article");
                    promoteItem.weburl = object1.optString("weburl");
                }
                playBean.promote.add(promoteItem);
            }
            list.add(playBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<BeijingBean> beijingJson(String json) {
        List<BeijingBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            BeijingBean beijingBean = new BeijingBean();

            beijingBean.articles = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.optJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                BeijingBean.Articles articles = beijingBean.new Articles();
                articles.auther_name = jsonObject2.optString("auther_name");
                articles.weburl = jsonObject2.optString("weburl");
                articles.title = jsonObject2.optString("title");
                if (jsonObject2.isNull("thumbnail_pic")) {
                    articles.thumbnail_pic = null;
                } else {
                    articles.thumbnail_pic = jsonObject2.optString("thumbnail_pic");
                }
                beijingBean.articles.add(articles);
            }

            JSONArray jsonArray1 = jsonObject1.optJSONArray("gallery");
            if (jsonArray1 != null) {
                beijingBean.gallery = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject3 = jsonArray1.optJSONObject(j);
                    BeijingBean.Gallery gallery = beijingBean.new Gallery();
                    gallery.promotion_img = jsonObject3.optString("promotion_img");
                    gallery.title = jsonObject3.optString("title");
                    gallery.weburl = jsonObject3.optString("weburl");
                    beijingBean.gallery.add(gallery);
                }
            }
            list.add(beijingBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Commbean> commJson(String json) {
        List<Commbean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            Commbean commbean = new Commbean();

            JSONObject jsonObject2 = jsonObject1.optJSONObject("discussion_info");
            commbean.title = jsonObject2.optString("title");
            JSONObject jsonObject3 = jsonObject1.optJSONObject("info");
            commbean.next_url = jsonObject3.optString("next_url");

            JSONArray jsonArray = jsonObject1.optJSONArray("posts");
            commbean.posts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject4 = jsonArray.optJSONObject(i);
                Commbean.PostsItem postsItem = commbean.new PostsItem();

                JSONObject jsonObject5 = jsonObject4.optJSONObject("auther");
                postsItem.icon = jsonObject5.optString("icon");
                postsItem.name = jsonObject5.optString("name");
                postsItem.content = jsonObject4.optString("content");
                postsItem.hot_num = jsonObject4.optInt("hot_num");
                postsItem.weburl = jsonObject4.optString("weburl");

                JSONArray jsonArray1 = jsonObject4.optJSONArray("medias");
                if (jsonArray1 != null) {
                    postsItem.medias = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject6 = jsonArray1.optJSONObject(j);
                        Commbean.MediasItem mediasItem = commbean.new MediasItem();
                        mediasItem.url = jsonObject6.optString("url");
                        postsItem.medias.add(mediasItem);
                    }
                }
                commbean.posts.add(postsItem);
            }
            list.add(commbean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<SpecialBean> specialJson(String json) {
        List<SpecialBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            SpecialBean specialBean = new SpecialBean();

            specialBean.next_url = jsonObject1.optString("next_url");

            specialBean.list = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.optJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                SpecialBean.ListItem listItem = specialBean.new ListItem();

                listItem.topic_list = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject2.optJSONArray("topic_list");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject3 = jsonArray1.optJSONObject(j);
                    SpecialBean.TopicItem topicItem = specialBean.new TopicItem();

                    topicItem.article = new ArrayList<>();
                    JSONArray jsonArray2 = jsonObject3.optJSONArray("article");
                    for (int x = 0; x < jsonArray2.length(); x++) {
                        JSONObject jsonObject4 = jsonArray2.optJSONObject(x);
                        SpecialBean.ArticleIt articleIt = specialBean.new ArticleIt();
                        JSONObject jsonObject5 = jsonObject4.optJSONObject("article");
                        articleIt.auther_name = jsonObject5.optString("auther_name");
                        articleIt.title = jsonObject5.optString("title");
                        articleIt.weburl = jsonObject5.optString("weburl");
                        topicItem.article.add(articleIt);
                    }

                    topicItem.entrance = new ArrayList<>();
                    JSONArray jsonArray3 = jsonObject3.optJSONArray("entrance");
                    for (int y = 0; y < jsonArray3.length(); y++) {
                        JSONObject jsonObject4 = jsonArray3.optJSONObject(y);
                        SpecialBean.EntranceItem entranceItem = specialBean.new EntranceItem();
                        JSONObject jsonObject5 = jsonObject4.optJSONObject("topic");
                        entranceItem.api_url = jsonObject5.optString("api_url");
                        entranceItem.block_title = jsonObject5.optString("block_title");
                        topicItem.entrance.add(entranceItem);
                    }

                    topicItem.gallery = new ArrayList<>();
                    JSONArray jsonArray4 = jsonObject3.optJSONArray("gallery");
                    if (jsonArray4 != null) {
                        for (int z = 0; z < jsonArray4.length(); z++) {
                            JSONObject jsonObject4 = jsonArray4.optJSONObject(z);
                            SpecialBean.GalleryItem galleryItem = specialBean.new GalleryItem();
                            galleryItem.promotion_img = jsonObject4.optString("promotion_img");

                            JSONObject jsonObject5 = jsonObject4.optJSONObject("article");
                            galleryItem.auther_name = jsonObject5.optString("auther_name");
                            galleryItem.title = jsonObject5.optString("title");
                            galleryItem.weburl = jsonObject5.optString("weburl");
                            topicItem.gallery.add(galleryItem);
                        }
                    }
                    listItem.topic_list.add(topicItem);
                }
                specialBean.list.add(listItem);
            }
            list.add(specialBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<SpBean> spJson(String json) {
        List<SpBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            SpBean spBean = new SpBean();
            JSONObject jsonObject2 = jsonObject1.optJSONObject("block_info");
            spBean.title = jsonObject2.optString("title");
            JSONObject jsonObject3 = jsonObject2.optJSONObject("diy");
            spBean.bgimage_url = jsonObject3.optString("bgimage_url");

            spBean.articles = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.optJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject4 = jsonArray.optJSONObject(i);
                SpBean.ArticlesI articlesI = spBean.new ArticlesI();
                articlesI.auther_name = jsonObject4.optString("auther_name");
                articlesI.title = jsonObject4.optString("title");
                articlesI.weburl = jsonObject4.optString("weburl");

                JSONArray jsonArray1 = jsonObject4.optJSONArray("media");
                if (jsonArray1 != null) {
                    articlesI.media = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject5 = jsonArray1.optJSONObject(j);
                        SpBean.MediaI mediaI = spBean.new MediaI();
                        mediaI.url = jsonObject5.optString("url");
                        articlesI.media.add(mediaI);
                    }
                }
                spBean.articles.add(articlesI);
            }
            list.add(spBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
