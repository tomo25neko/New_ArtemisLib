package org.new_artemis;

public class Reference
{
    //Essentials
    public static final String MODID = "newartemis";  // MODIDを新しいものに変更
    public static final String NAME = "New Artemis";  // MOD名を変更
    public static final String VERSION = "1.0.0";  // バージョンはそのまま
    public static final String ACCEPTEDVERSIONS = "[1.18,1.19]";  // 1.18.2に変更
    public static final String DEPENDENCIES = "required-after:forge@[40,44)";  // Forgeのバージョンを変更

    //Proxies
    public static final String CLIENT_PROXY_CLASS = "org.tomo25neko.new_artemis.proxy.ClientProxy";  // パッケージの修正
    public static final String COMMON_PROXY_CLASS = "org.tomo25neko.new_artemis.proxy.CommonProxy";  // パッケージの修正
}
