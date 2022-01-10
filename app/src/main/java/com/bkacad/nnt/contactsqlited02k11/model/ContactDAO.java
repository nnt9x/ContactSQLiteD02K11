package com.bkacad.nnt.contactsqlited02k11.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bkacad.nnt.contactsqlited02k11.database.DAO;
import com.bkacad.nnt.contactsqlited02k11.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements DAO<Contact> {

    private DBHelper dbHelper;

    public ContactDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Contact> all() {
        List<Contact> contacts = new ArrayList<>();

        String sql = "SELECT * FROM contacts";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Đọc dữ liệu -> thêm dữ liệu vào list
        if(cursor.moveToFirst()){
            // Lấy vị trí col theo tên
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexPhone = cursor.getColumnIndex("phone");
            do{
                Contact item = new Contact();
                item.setId(cursor.getLong(indexId));
                item.setName(cursor.getString(indexName));
                item.setPhone(cursor.getString(indexPhone));
                // Thêm danh sách
                contacts.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }

    @Override
    public Contact get(long id) {
        String sql = "SELECT * FROM contacts WHERE id = "+id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Contact item = null;
        if(cursor.moveToFirst()){
            item = new Contact();
            // Lấy vị trí col theo tên
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexPhone = cursor.getColumnIndex("phone");

            item.setId(cursor.getLong(indexId));
            item.setName(cursor.getString(indexName));
            item.setPhone(cursor.getString(indexPhone));
        }

        cursor.close();
        return item;
    }

    @Override
    public long create(Contact item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("phone", item.getPhone());
        long id = db.insert("contacts",null,values);
        return id;
    }

    @Override
    public int update(Contact item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("phone", item.getPhone());
        // UPDATE contacts SET name = 'name' , ... WHERE id =
        int rs = db.update("contacts", values, "id = "+item.getId(),null);

        return rs;
    }

    @Override
    public int delete(Contact item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rs = db.delete("contacts", "id = "+item.getId(), null);
        return rs;
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rs = db.delete("contacts", "id = "+ id, null);
        return rs;
    }
}
