var IITSign = {};

IITSign.getActiveXIITSign = function (src) {
    var result;
    try {
        if (this.isSignActivated == undefined) this.isSignActivated = false;
        if (this.isSignActivated == true) {
            formSign.ResetOperation();
            formSign.SetCodePage(65001);  // WideCharToMultiByte http://msdn.microsoft.com/en-us/library/windows/desktop/dd374130(v=vs.85).aspx
            formSign.ContinueHash(src);
            result = formSign.RawSignHash(formSign.EndHash());
        }
        else {
            location.replace('');
/*            if (this.activateActiveXIITSign(false) == true) {
                formSign.SetCodePage(65001);
                formSign.ContinueHash(src);
                result = formSign.RawSignHash(formSign.EndHash());
            }
            else {
                result = "Ошибка наложения цифровой подписи: ключ не активирован.";
            }*/
        }
    }
    catch (e) {
        document.getElementById('isSignActivated').value = false;
        result = "Ошибка наложения цифровой подписи. " + e.message;
        formSign.ResetOperation();
    }
    return result;
};

IITSign.activateActiveXIITSign = function (showAlert) {
    var activateResult = false;
    try {
        formSign.Initialize();
        formSign.ResetOperation();
        formSign.ResetPrivateKey();
        formSign.ReadPrivateKey();
        this.isSignActivated = true;
        activateResult = true;
        if (showAlert == true) {
            alert("Ключ активирован успешно");
        }
    }
    catch (e) {
        this.isSignActivated = false;
        if (showAlert == true) {
            alert("Ключ не активирован. " + e.message);
        }
    }
    return activateResult;
};

IITSign.getKeyOwnerClockNumber = function (showAlert) {
    try {
        if (this.isSignActivated == undefined) this.isSignActivated = false;
        if (this.isSignActivated != true) this.activateActiveXIITSign(false);
        if (this.isSignActivated == true) {
            return formSign.GetOwnCertificateUserCode();
        } else {
            result = "Ошибка чтения сертификата: ключ не активирован.";
        }
    }
    catch (e) {
        this.isSignActivated = false;
        if (showAlert == true) {
            alert("Ключ не активирован. " + e.message);
        }
    }
    return null;
};