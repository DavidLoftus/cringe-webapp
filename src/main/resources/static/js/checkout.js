(function () {
  'use strict';

  window.addEventListener('load', function () {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');

    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function (form) {
      form.addEventListener('submit', function (event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }, false);
})();

function processPayment(id, duration, callback) {
  var paymentProgress = document.getElementById(id);
  paymentProgress.className = 'progressbar';

  var progressInner = document.createElement('div');
  progressInner.className = 'inner';
  progressInner.style.animationDuration = duration;

  if (typeof(callback) === 'function') {
    progressInner.addEventListener('animationend', callback);
  }

  paymentProgress.appendChild(progressInner);
  progressInner.style.animationPlayState = 'running';
}

function beforeSubmit() {
  var form = document.getElementById("checkout-form");
  if(form.checkValidity() == true) {
    processPayment('progressBar', '5s', function() {
      document.getElementById("checkout-form").submit();
    });
  }
  form.classList.add('was-validated');
}