package com.poetry;

import java.util.List;
import java.util.Scanner;

import com.poetry.dao.IDao;
import com.poetry.dao.JokeConfigDao;
import com.poetry.dao.PoetryConfigDao;
import com.poetry.model.Config;
import com.poetry.model.PoetryConfig;
import com.poetry.util.AfVersion;
import com.xiaomi.infra.galaxy.client.GalaxyAdmin;
import com.xiaomi.infra.galaxy.common.auth.BasicGalaxyCredentials;
import com.xiaomi.infra.galaxy.common.auth.GalaxyCredentials;

public class ConfigPoetry {
	
	private static String[] mains = new  String[]{
		"1.列出所有",
		"2.添加",
		"3.删除",
		"4.编辑",
		"5.退出",
	};
	
	//http://222.85.149.6:8089/UpdateService/Projects/Poetry/WebDemo/::广告杀手

	private static String appId = "2882303761517235821";
	private static String appSecret = "UzOJQmxqP4Y20mzep9ZZoA==";


	private static String jappId = "2882303761517300142";
	private static String jappSecret = "YcqL/Ty9/SFQcN6hYuuptg==";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		System.out.println("选择应用：");
		System.out.println("1：为你写诗");
		System.out.println("2：经典笑话");
		System.out.println(new Config().toString());
		int app = new Scanner(System.in).nextInt();
		if (app == 2) {
			appId = jappId;
			appSecret = jappSecret;
		}
		
		GalaxyAdmin galaxy = GalaxyAdmin.createInstance(appId);
		GalaxyCredentials credenty = new BasicGalaxyCredentials(appId, appSecret);
		galaxy.setCredentials(credenty);
		IDao<Config> dao = new PoetryConfigDao(galaxy);
		if (app ==2) {
			 dao = new JokeConfigDao(galaxy);
		}
//		System.out.println(dao.getByName("poetry"));
		int index = 0;
		do {
			switch (index) {
			case 0:
				doShowAll(dao);
				break;
			case 1:
				doAddConfig(dao);
				break;
			case 2:
				doDelConfig(dao);
				break;
			case 3:
				doSetConfig(dao);
				break;
			}
			doPrintMainMenu();
			index = getMenuIndex();
		} while (index < 4);
	}

	@SuppressWarnings("resource")
	private static void doSetConfig(IDao<Config> dao) {
		// TODO Auto-generated method stub
		Config config = dao.newModel();
		System.out.println("\t请输入名称:");
		config.Name = new Scanner(System.in).nextLine();
		System.out.println("\t请输入内容:");
		config.HideAd = new Scanner(System.in).nextBoolean();
		try {
			System.out.println("\t正在更新..");
			dao.set(config);
			System.out.println("\t更新成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("\t更新失败");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void doDelConfig(IDao<Config> dao) {
		// TODO Auto-generated method stub
		PoetryConfig config = new PoetryConfig();
		System.out.println("\t请输入名称:");
		config.Name = new Scanner(System.in).nextLine();
		try {
			System.out.println("\t正在删除..");
			dao.del(config);
			System.out.println("\t删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("\t添加删除");
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("resource")
	private static void doAddConfig(IDao<Config> dao) {
		// TODO Auto-generated method stub
		PoetryConfig config = new PoetryConfig();
		System.out.println("\t请输入名称:");
		config.Name = new Scanner(System.in).nextLine();
		System.out.println("\t请输入内容:");
		config.HideAd = new Scanner(System.in).nextBoolean();
		System.out.println("\t请输入版本:");
		String verson = new Scanner(System.in).nextLine();
		config.Verson = AfVersion.transformVersion(verson);
		System.out.println("\t请输入备注:");
		config.Remark = new Scanner(System.in).nextLine();
		System.out.println("\t请输入WebApp:");
		config.Urls = new Scanner(System.in).nextLine();
		try {
			System.out.println("\t正在添加..");
			dao.add(config);
			System.out.println("\t添加成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("\t添加失败");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static int getMenuIndex() {
		// TODO Auto-generated method stub
		return new Scanner(System.in).nextInt()-1;
	}

	private static void doPrintMainMenu() {
		// TODO Auto-generated method stub
		for (String item : mains) {
			System.out.println(item);
		}
	}

	private static void doShowAll(IDao<Config> dao)  {
		// TODO Auto-generated method stub
		try {
			List<? extends Config> list = dao.getAll();
			for (Config item : list) {
				System.out.println((char)('a'+list.indexOf(item))+"."+item.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
