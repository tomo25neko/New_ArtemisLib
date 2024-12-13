package org.new_artemis.Entity;

public interface ISizedEntity {
    //サイズ取得メソッド
    double getEntitySize();

    // サイズ設定メソッド
    void setEntitySize(double size, boolean interpolate);
}
