package com.manning.aip.mash;

import android.net.Uri;

interface IMashService{
  Uri mash(in Uri uri, float scaleX, float scaleY, float angle);
}