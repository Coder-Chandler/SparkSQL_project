# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import DayUrlPvTraffic
from django.http import JsonResponse
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import authentication, permissions
from django.contrib.auth.models import User
from django.core import serializers
# Create your views here.

# def getdata(request):
#     # 使用ORM
#     # all()返回的是QuerySet 数据类型；values()返回的是ValuesQuerySet 数据类型
#     data = DayUrlPvTraffic.objects.values('url', 'traffic_sums', 'page_view', 'avg')
#     data = serializers.serialize("json", tomcats)
#     return JsonResponse(list(data), safe=False)

class HomeView(View):
    def get(self, request):
        return render(request, "index.html", {})


class Charts(APIView):
    authentication_classes = []
    permission_classes = []

    def get(self, request, format=None):
        day = DayUrlPvTraffic.objects.values("day")[:10]

        urls = DayUrlPvTraffic.objects.values("url").order_by("-traffic_sums")[:10]

        traffics = DayUrlPvTraffic.objects.values("traffic_sums").order_by("-traffic_sums")[:10]

        page_views = DayUrlPvTraffic.objects.values("page_view").order_by("-traffic_sums")[:10]

        avg = DayUrlPvTraffic.objects.values("avg").order_by("-traffic_sums")[:10]
        data = {
            'day': day,
            'urls': urls,
            'traffics': traffics,
            'page_views': page_views,
            'avg': avg,
        }
        return Response(data)
