package org.example;

import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DroolsTest {
    public static void main(String[] args) throws DroolsParserException,
            IOException {
//        DroolsTest droolsTest = new DroolsTest();
//        droolsTest.executeDrools();
        //預設容器
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        //一個任務
        KieSession kieSession = kContainer.newKieSession("rulesSession");
        //規則集合，可以包含多個 kiesession。
        KieBase kieBase= kContainer.getKieBase("am");
        System.out.println(""+kieBase);
//        Results result = kContainer.verify();
        //加入規則
        Product product = new Product();
        product.setPoint(89);
        kieSession.insert(product);
        ResultOutput output = new ResultOutput();
        kieSession.setGlobal("output",output);
        //執行規則
        kieSession.fireAllRules();
        System.out.println("Point:"+product.getPoint()+"\n"+
                "Rate:"+output.getRate());
    }
    public void executeDrools() throws DroolsParserException, IOException {

        PackageBuilder packageBuilder = new PackageBuilder();
        //抓取drool文件(resources)
        String ruleFile = "/rules/rule.drl";
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);
        System.out.println("Stream:"+resourceAsStream);
        //讀取
        Reader reader = new InputStreamReader(resourceAsStream);
        packageBuilder.addPackageFromDrl(reader);
        org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
        //規則庫
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(rulesPackage);
        //工作內存(包含規則加入、執行等方法)
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        //資料設置
        Product product = new Product();
        product.setPoint(55);
        Product product2 = new Product();
        product2.setPoint(100);
        //加入規則處理
        workingMemory.insert(product);
        workingMemory.insert(product2);
        //規則執行
        workingMemory.fireAllRules();

        System.out.println("Point:"+product.getPoint()+"\n"+
                "Rate:"+product.getRate()+"\n"+"============="+"\n"+
                "Point:"+product2.getPoint()+"\n"
        +"Rate:"+product2.getRate());
    }

}