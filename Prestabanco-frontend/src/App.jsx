import './App.css'
import {HashRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import SimulateCredit from './components/SimulateCredit';
import CreditApplication from './components/CreditApplication';
import UserRegister from './components/UserRegister';
import UserLogin from './components/UserLogin';
import CreditEvaluation from './components/CreditEvaluation';
import FollowCredits from './components/FollowCredits';

function App() {
  return (
      <Router>
          <div className="container">
          <Navbar></Navbar>
            <Routes>
              <Route path="/home" element={<Home/>} />
              <Route path="/credits/simulate" element={<SimulateCredit/>} />
              <Route path="/credits/create?userId=${userId}" element={<CreditApplication/>} />
              <Route path="/credits/getAll" element={<CreditEvaluation/>} />
              <Route path="/user/register" element={<UserRegister/>} />
              <Route path="/user/login" element={<UserLogin/>} />
              <Route path="/credits/create/:userId" element={<CreditApplication />} />
              <Route path="/credits/follow" element={<FollowCredits />} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App
