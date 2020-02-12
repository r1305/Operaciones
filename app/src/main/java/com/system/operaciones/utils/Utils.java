package com.system.operaciones.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {
    public static void openPdf(Context ctx, String pdf)
    {
        Uri uri2 = Uri.parse(pdf);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setData(uri2);
        ctx.startActivity(pdfOpenintent);
    }
}
