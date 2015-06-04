<%--
  Created by IntelliJ IDEA.
  User: Andrii
  Date: 5/29/2015
  Time: 12:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Диплом Коропець Андрій КА-15</title>
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="/resources/css/styles.css" />
  <script type="text/javascript">
    function result()
    {
      setTimeout(function() {
      var struct_vector = document.getElementById("struct_vector");
      struct_vector.style.display = "block";}, 2000)

      setTimeout(function() {
      var var_exentr = document.getElementById("var_exentr");
      var_exentr.style.display = "block";},2000)

      setTimeout(function() {
      var rank = document.getElementById("rank");
      rank.style.display = "block";},2000)

      setTimeout(function() {
      var graph = document.getElementById("graph_result");
      graph.style.display = "block";},5000)
    }
  </script>
  <style>
    #graph_result,#struct_vector,#var_exentr,#var_sladnis,#rank{display:none;}
  </style>
</head>
<body>

<div class="main_body">

  <header><h1>Будування симпліціальних комплексів</h1></header>

  <!-- вибір файлу -->
  <div class="input_file">
    <form enctype="multipart/form-data" method="post" >
      <center><p>
        <input type="file" name="file-name" >
        <input type="button" value="OK" onclick="result();">
      </p></center>
    </form>
  </div><!-- end вибір файлу -->


  <div class="wrapper">

    <!-- Ліва колонка шириною 3/12 -->
    <div class="colomn-3">
      <div class="row">
        <h3>Комплекс максимального порядку</h3>
        <li>
          <strong>Ранг:</strong>
          <p id="rank">3</p>
        </li>
      </div>
      <div class="row">
        <h3>Структурний вектор</h3>
        <li>
          <%--<strong>Структурний вектор:</strong>--%>
          <p id="struct_vector">2 2 3 1</p><!-- сюди виводиться змінна -->
        </li>
        <li>
          <strong>Ексцентриситет:</strong>
          <p id="var_exentr">1 0 1 1 0 0</p><!-- сюди виводиться змінна -->
        </li>
        <!--
        <li>
          <strong>Сткладність:</strong>
          <p id="var_sladnis">$var_sladnist</p> сюди виводиться змінна
        </li> -->
      </div>
      <!--
      <div class="row">
        <h3>Layot</h3>
        <p>text layot</p>
      </div> -->
    </div> <!-- end Ліва колонка -->



    <!-- Граф шириною 9/12 -->
    <div class="colomn-9">
      <div class="row">
        <h3>Graph Vizualization</h3>
        <div id="graph_result"><img style="width:100%;" src="resources/images/Bezymyannyy.png">
        </div>
      </div>
    </div><!-- end Граф -->





  </div><!-- end wrapper -->

</div><!-- end main_body -->

</body>
</html>
