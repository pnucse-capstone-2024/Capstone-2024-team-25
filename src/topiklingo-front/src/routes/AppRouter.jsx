import { Route, Routes } from 'react-router-dom';

import Footer from '@components/common/footer';
import NeedAuth from '@components/common/NeedAuth';
import MainLayout from '@components/layouts/MainLayout';
import UserList from '@pages/Admin/UserList';
import AiListening from '@pages/AiExam/AiListening';
import AiReading from '@pages/AiExam/AiReading';
import AiWriting from '@pages/AiExam/AiWriting';
import GoogleCallback from '@pages/Callback/GoogleCallback';
import About from '@pages/Company/About';
import CustomerService from '@pages/Contact/CustomService';
import ProductService from '@pages/Contact/ProductService';
import Exam from '@pages/Exam/Exam';
import Main from '@pages/Main/Main';
import Category from '@pages/MockExam/Category';
import MockExam from '@pages/MockExam/MockExam';
import TopikOne from '@pages/MockExam/TopikOne';
import TopikTwo from '@pages/MockExam/TopikTwo';
import TopikWriting from '@pages/MockExam/TopikWriting';
import ModifyExam from '@pages/ModifyExam/ModifyExam';
import MyPage from '@pages/MyPage/MyPage';
import NotFound from '@pages/NotFound/NotFound';
import PastExam from '@pages/PastExam/PastExam';
import Register from '@pages/Register/Register';

function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Main />} />
        <Route path="mock-exam" element={<MockExam />} />
        <Route path="past-exam" element={<PastExam />} />
      </Route>
      <Route path="/mypage" element={<MainLayout />}>
        <Route index element={<MyPage />} />
      </Route>
      <Route
        path="/admin"
        element={
          <NeedAuth auth="ADMIN">
            <MainLayout />
          </NeedAuth>
        }
      >
        <Route path="user-list" element={<UserList />} />
      </Route>
      <Route path="/callback/google" element={<GoogleCallback />} />
      <Route path="/register" element={<Register />} />
      <Route path="/company" element={<MainLayout />}>
        <Route path="about" element={<About />} />
      </Route>
      <Route path="/mock" element={<MainLayout />}>
        <Route path="topik1/listening" element={<TopikOne defaultType="listening" />} />
        <Route path="topik1/reading" element={<TopikOne defaultType="reading" />} />
        <Route path="topik2/listening" element={<TopikTwo defaultType="listening" />} />
        <Route path="topik2/reading" element={<TopikTwo defaultType="reading" />} />
        <Route path="topik2/writing" element={<TopikWriting defaultType="writing" />} />
        <Route path="combination-exam" element={<Category />} />
      </Route>
      <Route path="/ai-exam" element={<MainLayout />}>
        <Route path="reading" element={<AiReading />} />
        <Route path="listening" element={<AiListening />} />
        <Route path="writing" element={<AiWriting />} />
      </Route>
      <Route path="/contact" element={<MainLayout />}>
        <Route path="customer-service" element={<CustomerService />} />
        <Route path="product-service" element={<ProductService />} />
      </Route>
      <Route path="/exam" element={<Exam />} />
      <Route path="/modify-exam" element={<ModifyExam />} />
      <Route
        path="*"
        element={
          <>
            <NotFound />
            <Footer />
          </>
        }
      />
    </Routes>
  );
}

export default AppRouter;
