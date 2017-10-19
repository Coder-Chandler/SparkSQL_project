# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import DayUrlPvTraffic, IpStatistics, UrlCityStatistics
from rest_framework.views import APIView
from rest_framework.response import Response


class HomeView(View):
    def get(self, request):
        return render(request, "html/index.html", {})


class ChartJsView(View):
    def get(self, request):
        return render(request, "html/chart_js.html", {})


class EchartsView(View):
    def get(self, request):
        return render(request, "html/echarts.html", {})


class Charts(APIView):
    authentication_classes = []
    permission_classes = []

    def get(self, request, format=None):
        # DayUrlPvTraffic
        day = DayUrlPvTraffic.objects.values("day")[:8]
        urls_orderby_traffic = DayUrlPvTraffic.objects.values("url").order_by("-traffic_sums")[:8]
        urls_orderby_pageview = DayUrlPvTraffic.objects.values("url").order_by("-page_view")[:8]
        urls_orderby_avg = DayUrlPvTraffic.objects.values("url").order_by("-avg")[:8]
        traffics_pvtr = DayUrlPvTraffic.objects.values("traffic_sums").order_by("-traffic_sums")[:8]
        page_views_pvtr = DayUrlPvTraffic.objects.values("page_view").order_by("-page_view")[:8]
        avg = DayUrlPvTraffic.objects.values("avg").order_by("-avg")[:8]

        # IpStatistics
        ip_orderby_traffic = IpStatistics.objects.values("ip").order_by("-traffic_sums")[:8]
        ip_orderby_pageview = IpStatistics.objects.values("ip").order_by("-traffic_sums")[:8]
        traffic_ip = IpStatistics.objects.values("traffic_sums").order_by("-traffic_sums")[:8]
        page_views_ip = IpStatistics.objects.values("page_view").order_by("-page_view")[:8]

        # UrlCityStatistics
        

        x_traffic_0 = traffics[0].get("traffic_sums", 0)
        x_traffic_1 = traffics[1].get("traffic_sums", 0)
        x_traffic_2 = traffics[2].get("traffic_sums", 0)
        x_traffic_3 = traffics[3].get("traffic_sums", 0)
        x_traffic_4 = traffics[4].get("traffic_sums", 0)
        x_traffic_5 = traffics[5].get("traffic_sums", 0)

        x_pv_0 = page_views[0].get("page_view", 0)
        x_pv_1 = page_views[1].get("page_view", 0)
        x_pv_2 = page_views[2].get("page_view", 0)
        x_pv_3 = page_views[3].get("page_view", 0)
        x_pv_4 = page_views[4].get("page_view", 0)
        x_pv_5 = page_views[5].get("page_view", 0)

        x_avg_0 = avg[0].get("avg", 0)
        x_avg_1 = avg[1].get("avg", 0)
        x_avg_2 = avg[2].get("avg", 0)
        x_avg_3 = avg[3].get("avg", 0)
        x_avg_4 = avg[4].get("avg", 0)
        x_avg_5 = avg[5].get("avg", 0)

        y_url_traffic_0 = urls_orderby_traffic[0].get("url", "获取url字段失败")
        y_url_traffic_1 = urls_orderby_traffic[1].get("url", "获取url字段失败")
        y_url_traffic_2 = urls_orderby_traffic[2].get("url", "获取url字段失败")
        y_url_traffic_3 = urls_orderby_traffic[3].get("url", "获取url字段失败")
        y_url_traffic_4 = urls_orderby_traffic[4].get("url", "获取url字段失败")
        y_url_traffic_5 = urls_orderby_traffic[5].get("url", "获取url字段失败")

        y_url_pageview_0 = urls_orderby_pageview[0].get("url", "获取url字段失败")[:18]
        y_url_pageview_1 = urls_orderby_pageview[1].get("url", "获取url字段失败")[:18]
        y_url_pageview_2 = urls_orderby_pageview[2].get("url", "获取url字段失败")[:18]
        y_url_pageview_3 = urls_orderby_pageview[3].get("url", "获取url字段失败")[:18]
        y_url_pageview_4 = urls_orderby_pageview[4].get("url", "获取url字段失败")[:18]
        y_url_pageview_5 = urls_orderby_pageview[5].get("url", "获取url字段失败")[:18]

        y_url_avg_0 = urls_orderby_avg[0].get("url", "获取url字段失败")[30:48]
        y_url_avg_1 = urls_orderby_avg[1].get("url", "获取url字段失败")[30:48]
        y_url_avg_2 = urls_orderby_avg[2].get("url", "获取url字段失败")[30:48]
        y_url_avg_3 = urls_orderby_avg[3].get("url", "获取url字段失败")[30:48]
        y_url_avg_4 = urls_orderby_avg[4].get("url", "获取url字段失败")[30:48]
        y_url_avg_5 = urls_orderby_avg[5].get("url", "获取url字段失败")[30:48]

        url_traffic_data = [y_url_traffic_0, y_url_traffic_1, y_url_traffic_2, y_url_traffic_3, y_url_traffic_4, y_url_traffic_5]
        url_page_view_data = [y_url_pageview_0, y_url_pageview_1, y_url_pageview_2, y_url_pageview_3, y_url_pageview_4, y_url_pageview_5]
        url_avg_data = [y_url_avg_0, y_url_avg_1, y_url_avg_2, y_url_avg_3, y_url_avg_4, y_url_avg_5]
        traffic_data = [x_traffic_0, x_traffic_1, x_traffic_2, x_traffic_3, x_traffic_4, x_traffic_5]
        page_view_data = [x_pv_0, x_pv_1, x_pv_2, x_pv_3, x_pv_4, x_pv_5]
        avg_data = [x_avg_0, x_avg_1, x_avg_2, x_avg_3, x_avg_4, x_avg_5]
        data = {
            "url_traffic_data": url_traffic_data,
            "url_page_view_data": url_page_view_data,
            "url_avg_data": url_avg_data,
            "traffic_data": traffic_data,
            "page_view_data": page_view_data,
            "avg_data": avg_data
        }
        return Response(data)
